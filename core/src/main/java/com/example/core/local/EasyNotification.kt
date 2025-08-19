package com.example.core.local

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager

object EasyNotification {

    private const val TAG = "EasyNotification"

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun show(
        context: Context,
        title: String,
        message: String,
        channelId: String = "beany_channel",
        channelName: String = "Default",
        smallIcon: Int = android.R.drawable.ic_dialog_info,
        notificationId: Int = generateNotificationId()
    ): Int {
        Log.d(TAG, "Generated notification id: $notificationId")
        createNotificationChannel(context, channelId, channelName)

        // Create a deep link intent to your NavGraph
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("beanyapp://notification_screen/${Uri.encode(message)}")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        Log.d(TAG, "Preparing to show notification: id=$notificationId, title=$title, message=$message")

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
        Log.d(TAG, "Notification shown successfully: id=$notificationId")

        return notificationId
    }

    fun cancel(context: Context, notificationId: Int) {
        Log.d(TAG, "Cancelling notification: id=$notificationId")
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)
        WorkManager.getInstance(context).cancelAllWorkByTag(notificationId.toString())
    }

    fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "Creating notification channel: id=$channelId, name=$channelName")
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
            Log.d(TAG, "Notification channel created: id=$channelId")
        }
    }

    fun generateNotificationId(): Int {
        return (System.currentTimeMillis() % 10000).toInt()
    }
}