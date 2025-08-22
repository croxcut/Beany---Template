package com.example.domain.usecase

import com.example.domain.repository.local.NotificationRepository
import javax.inject.Inject

class CancelNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(notificationId: Int) {
        repository.cancelNotification(notificationId)
    }
}
