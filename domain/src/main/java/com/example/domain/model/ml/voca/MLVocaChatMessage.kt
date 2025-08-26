package com.example.domain.model.ml.voca

data class MLVocaChatMessage(
    val userMessage: String,
    val botResponse: String,
    val timestamp: Long = System.currentTimeMillis()
)