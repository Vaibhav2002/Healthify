package com.vaibhav.healthify.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.vaibhav.healthify.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val NOTIFICATION_ID = 12
        const val CHANNEL_NAME = "Healthify"
        const val CHANNEL_ID = "HealthifyChannel"
        const val NOTIFICATION_TITLE = "Drink Water!"
        const val NOTIFICATION_DESC = "Did you forget to drink water?"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)

        channel.also {
            it.setShowBadge(true)
            it.enableLights(true)
            it.enableVibration(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            channel.setAllowBubbles(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun getNotification(pendingIntent: PendingIntent): Notification =
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_DESC)
            .setSmallIcon(R.drawable.logo_small)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

    fun showNotification(pendingIntent: PendingIntent) {
        createNotificationChannel()
        val notification = getNotification(pendingIntent)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
