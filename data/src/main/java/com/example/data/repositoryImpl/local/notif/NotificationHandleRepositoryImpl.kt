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

package com.example.data.repositoryImpl.local.notif

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.core.local.EasyNotification
import com.example.core.local.NotificationWorker
import com.example.domain.repository.local.NotificationRepository
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationHandleRepositoryImpl @Inject constructor(
    private val context: Context
) : NotificationRepository {

    private val TAG = "NotificationRepo"

    override fun scheduleNotification(
        calendar: Calendar,
        title: String,
        message: String,
        repeatDaily: Boolean
    ) {
        val delay = calendar.timeInMillis - System.currentTimeMillis()
        if (delay <= 0) {
            Log.w(TAG, "Notification time is in the past. Skipping scheduling.")
            return
        }

        val notificationId = EasyNotification.generateNotificationId()
        Log.d(TAG, "Scheduling notification: id=$notificationId, title=$title, message=$message, delay=$delay ms, repeatDaily=$repeatDaily")

        val data = workDataOf(
            "title" to title,
            "message" to message,
            "notificationId" to notificationId
        )

        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(notificationId.toString())
            .build()

        WorkManager.Companion.getInstance(context).enqueue(notificationWork)
        Log.d(TAG, "One-time notification work enqueued: id=$notificationId")

        if (repeatDaily) {
            val repeatingWork = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(notificationId.toString())
                .build()

            WorkManager.Companion.getInstance(context).enqueueUniquePeriodicWork(
                "daily_notification_$notificationId",
                ExistingPeriodicWorkPolicy.REPLACE,
                repeatingWork
            )
            Log.d(TAG, "Repeating daily notification work enqueued: id=$notificationId")
        }
    }

    override fun cancelNotification(notificationId: Int) {
        Log.d(TAG, "Cancelling notification: id=$notificationId")
        EasyNotification.cancel(context, notificationId)
    }
}