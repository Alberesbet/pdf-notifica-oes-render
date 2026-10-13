package com.example.pdf

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/** Recebe mensagens push quando a Activity do PDF não está aberta. */
class PdfMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("users").document(uid)
            .set(mapOf("fcmToken" to token, "pushEnabled" to true), SetOptions.merge())
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val prefs = getSharedPreferences("pdf_private", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("setting_notificacoes", true)) return
        ensureChannels()
        val kind = message.data["kind"] ?: "message"
        // Não mostrar nome do remetente nem conteúdo privado na tela bloqueada.
        val title = "Nova notificação"
        val body = "Você recebeu uma nova notificação."
        val channel = if (kind == "call") "pdf_calls_beep_v3" else "pdf_messages_v2"
        val builder = NotificationCompat.Builder(this, channel)
            .setSmallIcon(R.drawable.ic_pdf)
            .setContentTitle(title)
            .setContentText(body)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setCategory(if (kind == "call") NotificationCompat.CATEGORY_CALL else NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            // Sem PendingIntent: tocar na notificação não abre a conversa.
        if (kind == "call") {
            // Chamadas em segundo plano também avisam com o som curto padrão de notificação.
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            }
        } else {
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            }
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify((System.currentTimeMillis() % 100000).toInt(), builder.build())
    }

    private fun ensureChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(NotificationManager::class.java)
        val sound = NotificationChannel(
            "pdf_messages_v2", "Notificações do PDF", NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Avisos de mensagens e mídias do PDF"
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build())
            enableVibration(false)
        }
        // Canal novo para substituir o antigo canal silencioso, pois o Android não permite
        // alterar o som de um canal já criado no aparelho.
        val callsWithBeep = NotificationChannel(
            "pdf_calls_beep_v3", "Avisos de chamadas", NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Avisos discretos de chamadas recebidas com som de notificação"
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build())
            enableVibration(false)
        }
        manager.createNotificationChannel(sound)
        manager.createNotificationChannel(callsWithBeep)
    }
}
