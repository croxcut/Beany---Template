package com.example.feature.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.ChatMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    // Show error snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(error)
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MLVoca Chat") },
                actions = {
                    if (uiState.messages.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.clearChat() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear chat"
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            ChatInputSection(
                inputText = uiState.inputText,
                isLoading = uiState.isLoading,
                onInputTextChange = viewModel::updateInputText,
                onSendMessage = viewModel::sendMessage
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.messages.isEmpty() && !uiState.isLoading) {
                WelcomeMessage()
            } else {
                ChatMessagesList(
                    messages = uiState.messages,
                    lazyListState = lazyListState,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp)
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadChatHistory()
    }
}

@Composable
fun WelcomeMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to MLVoca Chat",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Start a conversation by typing a message below",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun ChatMessagesList(
    messages: List<ChatMessage>,
    lazyListState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier,
        reverseLayout = false
    ) {
        items(messages) { message ->
            ChatMessageItem(
                message = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = message.userMessage,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = if (message.isUser) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
fun ChatInputSection(
    inputText: String,
    isLoading: Boolean,
    onInputTextChange: (String) -> Unit,
    onSendMessage: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = onInputTextChange,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text("Type your message...") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            enabled = !isLoading,
            maxLines = 3
        )

        IconButton(
            onClick = {
                if (inputText.isNotBlank() && !isLoading) {
                    onSendMessage(inputText)
                }
            },
            enabled = inputText.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send message"
                )
            }
        }
    }
}