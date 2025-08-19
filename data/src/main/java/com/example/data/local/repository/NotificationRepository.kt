package com.example.data.local.repository

import com.example.domain.model.ScheduledNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun insert(notification: ScheduledNotification)
    suspend fun delete(id: Int)
    fun getAllNotifications(): Flow<List<ScheduledNotification>>
}