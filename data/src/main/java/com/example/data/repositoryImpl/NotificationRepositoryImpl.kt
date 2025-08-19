package com.example.data.repositoryImpl

import android.content.Context
import android.util.Log
import com.example.domain.repository.NotificationRepository
import androidx.work.*
import com.example.core.local.EasyNotification
import com.example.core.local.NotificationWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class NotificationRepositoryImpl @Inject constructor(
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

        WorkManager.getInstance(context).enqueue(notificationWork)
        Log.d(TAG, "One-time notification work enqueued: id=$notificationId")

        if (repeatDaily) {
            val repeatingWork = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(notificationId.toString())
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
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