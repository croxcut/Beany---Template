package com.example.domain.model.ml.voca

data class UsageData(
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
)