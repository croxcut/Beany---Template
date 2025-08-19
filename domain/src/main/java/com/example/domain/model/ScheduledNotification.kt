package com.example.domain.model

data class ScheduledNotification(
    val id: Int = 0,
    val title: String,
    val message: String,
    val scheduledAt: Long,
    val repeatDaily: Boolean
)