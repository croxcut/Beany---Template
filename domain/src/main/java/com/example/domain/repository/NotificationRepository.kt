package com.example.domain.repository

import java.util.Calendar

interface NotificationRepository {
    fun scheduleNotification(
        calendar: Calendar,
        title: String,
        message: String,
        repeatDaily: Boolean
    )
    fun cancelNotification(notificationId: Int)
}