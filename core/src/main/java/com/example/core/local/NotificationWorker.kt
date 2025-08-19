package com.example.core.local

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.core.R


class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        private const val TAG = "NotificationWorker"
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun doWork(): Result {
        Log.d(TAG, "Worker started")

        val title = inputData.getString("title") ?: "Title"
        val message = inputData.getString("message") ?: "Message"
        val notificationId = inputData.getInt("notificationId", EasyNotification.generateNotificationId())

        Log.d(TAG, "Title: $title, Message: $message, NotificationId: $notificationId")

        return try {
            // Show the notification using EasyNotification
            EasyNotification.show(
                context = applicationContext,
                title = title,
                message = message,
                channelId = "beany_channel", // must match the channel used elsewhere
                channelName = "Beany Notifications",
                notificationId = notificationId
            )
            Log.d(TAG, "Notification shown successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to show notification", e)
            Result.failure()
        }
    }
}