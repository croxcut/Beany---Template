package com.example.domain.usecase

import com.example.domain.repository.local.NotificationRepository
import java.util.Calendar
import javax.inject.Inject

class ScheduleNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(calendar: Calendar, title: String, message: String, repeatDaily: Boolean) {
        repository.scheduleNotification(calendar, title, message, repeatDaily)
    }
}