package com.example.domain.model.ml.voca

data class MLVocaRequest(
    val prompt: String,
    val model: String = "tinyllama",
    val max_tokens: Int? = null,
    val temperature: Double? = 0.1
)