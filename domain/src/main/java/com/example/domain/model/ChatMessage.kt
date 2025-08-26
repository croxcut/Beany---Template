package com.example.domain.model

data class ChatMessage(
    val userMessage: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)