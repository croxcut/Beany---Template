package com.example.domain.model.db

import java.util.Date

data class Note(
    val content: String,
    val createdAt: Date = Date()
)