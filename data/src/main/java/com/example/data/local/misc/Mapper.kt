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

// Entity -> Domain
fun NotificationEntity.toDomain(): ScheduledNotification {
    return ScheduledNotification(
        id = this.id,
        title = this.title,
        message = this.message,
        scheduledAt = this.scheduledAt,
        repeatDaily = this.repeatDaily
    )
}