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

package com.example.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.repository.NotificationRepository
import com.example.data.model.NotificationEntity
import com.example.domain.model.ScheduledNotification
import com.example.domain.usecase.CancelNotificationUseCase
import com.example.domain.usecase.ScheduleNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.flow.map
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository,
    private val scheduleNotificationUseCase: ScheduleNotificationUseCase,
    private val cancelNotificationUseCase: CancelNotificationUseCase
) : ViewModel() {

    // The repository already returns Flow<List<ScheduledNotification>>
    val notifications: StateFlow<List<ScheduledNotification>> =
        repository.getAllNotifications()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addNotification(notification: ScheduledNotification) {
        viewModelScope.launch {
            repository.insert(notification)
        }
    }

    fun removeNotification(id: Int, notificationId: Int? = null) {
        viewModelScope.launch {
            // Delete from DB
            repository.delete(id)

            // Cancel actual notification if notificationId is provided
            notificationId?.let {
                cancelNotificationUseCase(it)
            }
        }
    }

    fun scheduleNotification(
        calendar: Calendar,
        title: String,
        message: String,
        repeatDaily: Boolean
    ) {
        viewModelScope.launch {
            // Schedule the system notification and get its ID
            val systemNotificationId = scheduleNotificationUseCase(calendar, title, message, repeatDaily)

            // Save in DB as ScheduledNotification, not NotificationEntity directly
            val notification = ScheduledNotification(
                title = title,
                message = message,
                scheduledAt = calendar.timeInMillis,
                repeatDaily = repeatDaily
            )
            repository.insert(notification)
        }
    }
}