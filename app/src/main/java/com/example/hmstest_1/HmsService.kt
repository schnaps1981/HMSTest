package com.example.hmstest_1

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import kotlin.random.Random

class HmsService : HmsMessageService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            startNotification(it.body.orEmpty(), it.title.orEmpty())
        }
    }

    override fun onNewToken(token: String) {
        Log.d("TAG", "Token $token")
        super.onNewToken(token)
    }

    private fun startNotification(text: String, title: String) {
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = this.getString(R.string.DEFAULT_NOTIFICATION_CHANNEL_ID)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                this.getString(R.string.notification_channel_name_notify),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.lightColor = ContextCompat.getColor(this, R.color.mango)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val intent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat
            .Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setContentIntent(intent)
            .setSmallIcon(R.drawable.ic_notification_light)
            .build()

        notificationManager.notify(Random.nextInt(10000), notification)
    }
}
