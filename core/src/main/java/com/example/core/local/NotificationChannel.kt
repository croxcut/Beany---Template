package com.example.core.local

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build


fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "beany_channel"
        val channelName = "Beany Notifications"
        val channelDescription = "Notifications for Beany app"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}