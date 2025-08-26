package com.example.feature.test


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ChatMessage
import com.example.domain.model.ml.voca.MLVocaChatMessage
import com.example.domain.repository.remote.mlvoca.MLVocaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val mlVocaRepository: MLVocaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(message: String) {
        if (message.isBlank()) return

        viewModelScope.launch {
            val currentMessages = _uiState.value.messages.toMutableList()
            currentMessages.add(ChatMessage(userMessage = message, isUser = true))

            _uiState.value = _uiState.value.copy(
                messages = currentMessages,
                isLoading = true,
                inputText = ""
            )

            val result = mlVocaRepository.sendMessage(message)

            result.onSuccess { response ->
                val updatedMessages = _uiState.value.messages.toMutableList()
                updatedMessages.add(ChatMessage(userMessage = response, isUser = false))

                _uiState.value = _uiState.value.copy(
                    messages = updatedMessages,
                    isLoading = false,
                    error = null
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun updateInputText(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun loadChatHistory() {
        viewModelScope.launch {
            val history = mlVocaRepository.getChatHistory()
            val chatMessages = mutableListOf<ChatMessage>()

            history.forEach { chatHistoryItem ->
                chatMessages.add(ChatMessage(userMessage = chatHistoryItem.userMessage, isUser = true))
                chatMessages.add(ChatMessage(userMessage = chatHistoryItem.botResponse, isUser = false))
            }

            _uiState.value = _uiState.value.copy(messages = chatMessages)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            mlVocaRepository.clearChatHistory()
            _uiState.value = _uiState.value.copy(messages = emptyList())
        }
    }
}

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val inputText: String = "",
    val error: String? = null
)