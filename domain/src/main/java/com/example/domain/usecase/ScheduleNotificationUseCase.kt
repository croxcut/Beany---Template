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