package com.example.domain.model

data class ActivityEntity(
    val id: Int = 0,
    val activity: String,
    val date: Long // store timestamp, format in UI
)
