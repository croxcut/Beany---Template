package com.example.feature.community.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.ui.theme.Beige1
import com.example.domain.model.Post
import com.example.domain.model.Profile
import com.example.domain.model.Reply
import com.example.feature.community.viewModels.PostDetailViewModel

@Composable
fun PostDetailPage(
    postId: Long,
    viewModel: PostDetailViewModel = hiltViewModel()
) {
    val post by viewModel.post
    val replies by remember { derivedStateOf { viewModel.replies } }
    var newReply by remember { mutableStateOf("") }

    val profiles by viewModel.profiles.collectAsState()
    val sesson by viewModel.profile.collectAsState()

    LaunchedEffect(postId) {
        viewModel.loadSession()
        viewModel.loadProfiles()
        viewModel.loadPost(postId)
        viewModel.loadReplies(postId)
    }

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
            item {
                post?.let { postItem ->
                    profiles.let {
                        PostCard(
                            post = postItem,
                            replies = replies,
                            onReplyClick = viewModel::setParentReplyId,
                            onUnsendReply = viewModel::unsendReply,
                            profiles = profiles,
                            sessionId = sesson?.id.toString()
                        )
                    }
                }
            }
        }

        ReplyInput(
            parentReplyId = viewModel.parentReplyId.value,
            replies = replies,
            profiles = profiles,
            newReply = newReply,
            onNewReplyChange = { newReply = it },
            onSendReply = {
                viewModel.createReply(postId, newReply)
                newReply = ""
            }
        )
    }
}

@Composable
private fun formatUserRole(profile: Profile?): String {
    return when {
        profile == null -> ""
        profile.registeredAs == "Administrator" -> "[Administrator]"
        profile.registeredAs == "Expert" && profile.verified == true -> "[Expert]"
        profile.registeredAs == "Farmer" -> "Farmer"
        else -> ""
    }
}

@Composable
private fun PostCard(
    post: Post,
    replies: List<Reply>,
    onReplyClick: (Long) -> Unit,
    onUnsendReply: (Long) -> Unit,
    profiles: List<Profile>,
    sessionId: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            val postProfile = profiles.find { it.id == post.sender }
            val displayName = postProfile?.username ?: "Unknown"
            val userRole = formatUserRole(postProfile)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(displayName, fontWeight = FontWeight.Bold)
                if (userRole.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userRole,
                        color = if (userRole == "[Administrator]") Color.Red else Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.height(4.dp))
            Text(post.post_title, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(post.post_body ?: "")
            Spacer(Modifier.height(8.dp))

            replies.filter { it.parent_reply_id == null }.forEach { reply ->
                ReplyThread(
                    reply = reply,
                    replies = replies,
                    onReplyClick = onReplyClick,
                    onUnsendReply = onUnsendReply,
                    indent = 1,
                    profiles = profiles,
                    currentUserId = sessionId
                )
            }
        }
    }
}


@Composable
private fun ReplyInput(
    parentReplyId: Long?,
    replies: List<Reply>,
    profiles: List<Profile>,
    newReply: String,
    onNewReplyChange: (String) -> Unit,
    onSendReply: () -> Unit
) {
    Column {
        parentReplyId?.let { parentId ->
            replies.find { it.id == parentId }?.let { parent ->
                val parentProfile = profiles.find { it.id == parent.sender }
                ParentReplyPreview(parent = parent, profile = parentProfile)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newReply,
                onValueChange = onNewReplyChange,
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
                onClick = onSendReply,
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Reply")
            }
        }
    }
}

@Composable
private fun ParentReplyPreview(
    parent: Reply,
    profile: Profile?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E5EA))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            val displayName = profile?.username ?: "Unknown"
            val userRole = formatUserRole(profile)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$displayName:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                if (userRole.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userRole,
                        color = if (userRole == "[Administrator]") Color.Red else Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }
            Text(parent.reply_body, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ReplyThread(
    reply: Reply,
    replies: List<Reply>,
    onReplyClick: (Long) -> Unit,
    onUnsendReply: (Long) -> Unit,
    indent: Int,
    profiles: List<Profile>,
    currentUserId: String
) {
    val maxIndent = 1
    val actualIndent = minOf(indent, maxIndent)
    var showDialog by remember { mutableStateOf(false) }

    val canUnsend = reply.reply_body != "Unsent a message" && reply.sender == currentUserId

    val parentReply = reply.parent_reply_id?.let { parentId ->
        replies.find { it.id == parentId }
    }

    val replyProfile = profiles.find { it.id == reply.sender }
    val replyDisplayName = replyProfile?.username ?: "Unknown"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (actualIndent * 12).dp, end = 4.dp)
            .clickable { onReplyClick(reply.id) }
    ) {
        parentReply?.let { parent ->
            val parentProfile = profiles.find { it.id == parent.sender }
            val parentDisplayName = parentProfile?.username ?: "Unknown"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .offset(x = (-10).dp, y = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Beige1)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(parentDisplayName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(parent.reply_body, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Box(
            modifier = Modifier
                .background(
                    color = if (actualIndent == 0) Color(0xFFD8EAFB) else Color(0xFFF1F0F0),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    val replyProfile = profiles.find { it.id == reply.sender }
                    val userRole = formatUserRole(replyProfile)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(replyDisplayName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        if (userRole.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = userRole,
                                color = when {
                                    userRole == "[Administrator]" -> Color.Red
                                    userRole == "[Expert]" -> Color(0xFF6200EE)
                                    else -> Color.Gray
                                },
                                fontSize = 10.sp
                            )
                        }
                    }
                    Text(reply.reply_body, fontSize = 14.sp)
                }

                if (canUnsend) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Unsend Reply",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { showDialog = true },
                        tint = Color.Red
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Unsend Message") },
                text = { Text("Do you want to unsend this message?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onUnsendReply(reply.id)
                            showDialog = false
                        }
                    ) { Text("Yes") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("No") }
                }
            )
        }

        replies.filter { it.parent_reply_id == reply.id }.forEach { child ->
            ReplyThread(
                reply = child,
                replies = replies,
                onReplyClick = onReplyClick,
                onUnsendReply = onUnsendReply,
                indent = indent + 1,
                profiles = profiles,
                currentUserId = currentUserId
            )
        }
    }
}