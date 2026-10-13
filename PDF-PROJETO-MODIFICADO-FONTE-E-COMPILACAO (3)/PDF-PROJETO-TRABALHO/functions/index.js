const { onDocumentWritten } = require("firebase-functions/v2/firestore");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore, FieldValue } = require("firebase-admin/firestore");
const { getMessaging } = require("firebase-admin/messaging");

initializeApp();
const db = getFirestore();

async function sendPrivatePush(uid, kind) {
  if (!uid) return false;
  const userRef = db.collection("users").doc(uid);
  const userSnap = await userRef.get();
  const user = userSnap.data() || {};
  const token = user.fcmToken;
  if (!token || user.pushEnabled === false) return false;
  try {
    await getMessaging().send({
      token,
      data: { kind, title: "Nova notificação", body: "Você recebeu uma nova notificação." },
      android: { priority: "high", ttl: 60 * 1000 }
    });
    return true;
  } catch (error) {
    if (error && (error.code === "messaging/registration-token-not-registered" || error.code === "messaging/invalid-registration-token")) {
      await userRef.set({ fcmToken: FieldValue.delete() }, { merge: true });
    } else {
      console.error("Falha ao enviar push privado", error);
    }
    return false;
  }
}

// Mensagens: texto é notificado ao ser criado; mídia só depois que todos os blocos subirem.
exports.notifyNewMessage = onDocumentWritten("conversations/{conversationId}/messages/{messageId}", async (event) => {
  const before = event.data.before.exists ? event.data.before.data() : null;
  const after = event.data.after.exists ? event.data.after.data() : null;
  if (!after || after.notificationSent === true) return;
  const isTextCreated = !before && !after.attachment;
  const isMediaReady = !!after.attachment && after.attachmentPending === false && (!before || before.attachmentPending !== false);
  if (!isTextCreated && !isMediaReady) return;
  if (!after.recipientUid || after.senderUid === after.recipientUid) return;
  const sent = await sendPrivatePush(after.recipientUid, "message");
  if (sent) await event.data.after.ref.set({ notificationSent: true }, { merge: true });
});

// Chamada: envia aviso apenas quando a oferta WebRTC já existe, evitando avisar antes da oferta.
exports.notifyIncomingCall = onDocumentWritten("calls/{callId}", async (event) => {
  const before = event.data.before.exists ? event.data.before.data() : null;
  const after = event.data.after.exists ? event.data.after.data() : null;
  if (!after || after.notificationSent === true) return;
  if (after.status !== "ringing" || !after.calleeUid || !after.offerSdp) return;
  if (before && before.offerSdp) return;
  const sent = await sendPrivatePush(after.calleeUid, "call");
  if (sent) await event.data.after.ref.set({ notificationSent: true }, { merge: true });
});
