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

package com.example.data.local.misc

import com.example.data.model.NotificationEntity
import com.example.domain.model.ScheduledNotification

fun ScheduledNotification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = this.id,
        title = this.title,
        message = this.message,
        scheduledAt = this.scheduledAt,
        repeatDaily = this.repeatDaily
    )
}

fun NotificationEntity.toDomain(): ScheduledNotification {
    return ScheduledNotification(
        id = this.id,
        title = this.title,
        message = this.message,
        scheduledAt = this.scheduledAt,
        repeatDaily = this.repeatDaily
    )
}