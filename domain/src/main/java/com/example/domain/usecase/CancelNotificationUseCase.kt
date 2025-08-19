package com.example.domain.usecase

import com.example.domain.repository.NotificationRepository
import java.util.Calendar
import javax.inject.Inject

class CancelNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(notificationId: Int) {
        repository.cancelNotification(notificationId)
    }
}
