package com.vaibhav.healthify.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vaibhav.healthify.ui.homeScreen.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaterBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val REQUEST_CODE = 12
    }

    override fun onReceive(context: Context, inte: Intent?) {
        val notificationHelper = NotificationHelper(context)
        val intent = Intent(context, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, 0)
        notificationHelper.showNotification(pendingIntent)
    }
}
