package com.example.feature.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature.community.viewmodels.PostDetailViewModel

@Composable
fun PostDetailScreen(
    postId: Long,
    viewModel: PostDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(postId) { viewModel.observe(postId) }

    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Post card
            item {
                state.post?.let { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Tapping the post sets parentReplyId = null to indicate reply to post
                                viewModel.replyToPost(post.id)
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(post.sender, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(post.post_body ?: "")
                        }
                    }
                }
            }

            // Replies
            items(state.replies, key = { it.id }) { reply ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(reply.sender, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(reply.reply_body)
                    }
                }
            }
        }

        // Reply bar
        Column {
            // Show who we're replying to
            val replyTarget = when {
                state.parentReplyId == state.post?.id -> "Post"
                state.parentReplyId != null -> state.replies.find { it.id == state.parentReplyId }?.sender
                else -> null
            }
            replyTarget?.let { target ->
                Text(
                    text = "Replying to: $target",
                    color = Color.Blue,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = state.newReply,
                    onValueChange = viewModel::updateNewReply,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    placeholder = { Text("Write a reply...") },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Button(
                    onClick = { viewModel.sendReply(postId) },
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Reply")
                }
            }
        }
    }
}