package com.pangtrue.firebase_app

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessageService : FirebaseMessagingService() {

    // Foreground에서 Push Service를 받기 위해 Notification을 설정한다.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification.let { message ->
            val messageTitle = message!!.title
            val messageContent = message.body
            val mainIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val mainPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0)
            val builder1 = NotificationCompat.Builder(this, MainActivity.channel_id)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(messageTitle)
                .setContentText(messageContent)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)

            NotificationManagerCompat.from(this).apply {
                notify(101, builder1.build())
            }
        }
    }

    // 새로운 토큰이 생성될 때마다 해당 콜백이 호출된다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // 새로운 토큰 수신 시 서버로 전송
        MainActivity.uploadToken(token)
    }
}