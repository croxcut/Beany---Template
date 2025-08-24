// ===============================================================================
//
// Copyright (C) 2025-2026 by John Paul Valenzuela
//
// This source is available for distribution and/or modification
// only under the terms of the Beany Source Code License as
// published by Beany. All rights reserved.
//
// The source is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// FITNESS FOR A PARTICULAR PURPOSE. See the Beany Source Code License
// for more details.
//
// ===============================================================================

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