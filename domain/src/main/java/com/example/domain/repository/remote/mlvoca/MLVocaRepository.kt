package com.example.domain.repository.remote.mlvoca

import com.example.domain.model.ml.voca.MLVocaChatMessage

interface MLVocaRepository {
    suspend fun sendMessage(message: String): Result<String>
    suspend fun getChatHistory(): List<MLVocaChatMessage>
    suspend fun clearChatHistory()
}