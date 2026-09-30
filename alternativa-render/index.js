// Ponte experimental para enviar avisos FCM sem Cloud Functions.
// Requer credenciais de conta de serviço guardadas como variável secreta no servidor.
const express = require('express');
const admin = require('firebase-admin');
const app = express();
app.get('/', (_req, res) => res.status(200).send('PDF push bridge online'));
app.get('/health', (_req, res) => res.json({ ok: true }));

if (!process.env.FIREBASE_SERVICE_ACCOUNT_JSON) {
  console.error('Falta configurar FIREBASE_SERVICE_ACCOUNT_JSON como segredo.');
  process.exit(1);
}
const serviceAccount = JSON.parse(process.env.FIREBASE_SERVICE_ACCOUNT_JSON);
admin.initializeApp({ credential: admin.credential.cert(serviceAccount) });
const db = admin.firestore();
const messaging = admin.messaging();
const seen = new Set();

async function sendPrivatePush(uid, kind) {
  if (!uid) return false;
  const userRef = db.collection('users').doc(uid);
  const snap = await userRef.get();
  const user = snap.data() || {};
  if (!user.fcmToken || user.pushEnabled === false) return false;
  try {
    await messaging.send({
      token: user.fcmToken,
      data: { kind, title: 'Nova notificação', body: 'Você recebeu uma nova notificação.' },
      android: { priority: 'high', ttl: 60 * 1000 }
    });
    return true;
  } catch (e) {
    console.error('Falha no envio FCM:', e.code || e.message);
    return false;
  }
}

// Ignora o lote inicial para não notificar mensagens antigas ao iniciar o servidor.
let firstMessagesSnapshot = true;
db.collectionGroup('messages').onSnapshot(async (snapshot) => {
  if (firstMessagesSnapshot) { firstMessagesSnapshot = false; return; }
  for (const change of snapshot.docChanges()) {
    if (change.type !== 'added' && change.type !== 'modified') continue;
    const doc = change.doc;
    const m = doc.data();
    if (m.notificationSent === true || !m.recipientUid || m.senderUid === m.recipientUid) continue;
    const isTextCreated = change.type === 'added' && !m.attachment;
    const isMediaReady = !!m.attachment && m.attachmentPending === false;
    if (!isTextCreated && !isMediaReady) continue;
    const key = 'message:' + doc.ref.path;
    if (seen.has(key)) continue;
    seen.add(key);
    if (await sendPrivatePush(m.recipientUid, 'message')) {
      await doc.ref.set({ notificationSent: true }, { merge: true });
    }
  }
}, (e) => console.error('Listener de mensagens:', e.message));

let firstCallsSnapshot = true;
db.collection('calls').onSnapshot(async (snapshot) => {
  if (firstCallsSnapshot) { firstCallsSnapshot = false; return; }
  for (const change of snapshot.docChanges()) {
    if (change.type !== 'added' && change.type !== 'modified') continue;
    const doc = change.doc;
    const c = doc.data();
    if (c.notificationSent === true || c.status !== 'ringing' || !c.calleeUid || !c.offerSdp) continue;
    const key = 'call:' + doc.id;
    if (seen.has(key)) continue;
    seen.add(key);
    if (await sendPrivatePush(c.calleeUid, 'call')) {
      await doc.ref.set({ notificationSent: true }, { merge: true });
    }
  }
}, (e) => console.error('Listener de chamadas:', e.message));

const port = process.env.PORT || 10000;
app.listen(port, '0.0.0.0', () => console.log(`PDF push bridge na porta ${port}`));
