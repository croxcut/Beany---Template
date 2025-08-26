package com.example.data.repositoryImpl.remote.mlvoca

import com.example.data.remote.services.MLVocaApiService
import com.example.domain.model.ml.voca.MLVocaChatMessage
import com.example.domain.model.ml.voca.MLVocaRequest
import com.example.domain.repository.remote.mlvoca.MLVocaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MLVocaRepositoryImpl @Inject constructor(
    private val apiService: MLVocaApiService
) : MLVocaRepository {

    private val chatHistory = mutableListOf<MLVocaChatMessage>()

    override suspend fun sendMessage(message: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = MLVocaRequest(prompt = message)
                val result = apiService.generateResponseStream(request)

                result.onSuccess { response ->
                    chatHistory.add(MLVocaChatMessage(
                        userMessage = message,
                        botResponse = response
                    ))
                }

                result
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getChatHistory(): List<MLVocaChatMessage> {
        return chatHistory.toList()
    }

    override suspend fun clearChatHistory() {
        chatHistory.clear()
    }
}