package com.example.domain.model.ml.voca

data class MLVocaResponse(
    val response: String,
    val status: String,
    val usage: UsageData?
)