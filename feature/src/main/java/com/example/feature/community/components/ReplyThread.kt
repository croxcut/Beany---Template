// ===============================================================================
//
// Copyright (C) 2025-2026 by John Paul Valenzuela
//
// This source is available for distribution and/or modification
// only under the terms of the Beany Source Code License as
// published by Beany. All rights reserved.
//
// The source is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// FITNESS FOR A PARTICULAR PURPOSE. See the Beany Source Code License
// for more details.
//
// ===============================================================================

package com.example.feature.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.ui.theme.Brown1
import com.example.domain.model.supabase.Profile
import com.example.domain.model.supabase.Reply
import com.example.feature.R
import com.example.feature.community.misc.ParentReplyInThread
import com.example.feature.community.misc.formatUserRole
import com.example.feature.community.viewModels.PostDetailViewModel

@Composable
fun ReplyThread(
    reply: Reply,
    replies: List<Reply>,
    onReplyClick: (Long) -> Unit,
    onUnsendReply: (Long) -> Unit,
    onToggleLike: (Long) -> Unit,
    indent: Int,
    profiles: List<Profile>,
    currentUserId: String,
    viewModel: PostDetailViewModel,
    onProfileClick: (Profile) -> Unit
) {
    val maxVisualIndent = 3 // Hard cap for visual indentation
    var showNestedReplies by remember { mutableStateOf(false) }
    val actualIndent = if (indent > 0 && indent <= maxVisualIndent) 16.dp else 0.dp
    var showDialog by remember { mutableStateOf(false) }
    val canUnsend = reply.reply_body != "Unsent a message" && reply.sender == currentUserId
    val isLiked = remember(reply.likes) { reply.likes?.contains(currentUserId) ?: false }
    val likeCount = remember(reply.likes) { reply.likes?.size ?: 0 }
    val replyProfile = profiles.find { it.id == reply.sender }
    val context = LocalContext.current
    val nestedReplies = replies.filter { it.parent_reply_id == reply.id }

    val parentReply = reply.parent_reply_id?.let { parentId ->
        replies.find { it.id == parentId }
    }
    val parentProfile = parentReply?.let { reply ->
        profiles.find { it.id == reply.sender }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = actualIndent)
    ) {
        parentReply?.let { parent ->
            ParentReplyInThread(
                parent = parent,
                profile = parentProfile,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Box(
            modifier = Modifier
                .background(
                    color = if (indent == 0) Color(0xFFD8EAFB) else Color(0xFFF1F0F0),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            replyProfile?.let { onProfileClick(it) }
                        }
                ) {
                    val profileImageUri by viewModel.getProfileImageUri(replyProfile?.id).collectAsState()

                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(profileImageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Picture",
                        placeholder = painterResource(R.drawable.plchldr),
                        error = painterResource(R.drawable.plchldr),
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.dp, Brown1, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = replyProfile?.username ?: "Unknown",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = formatUserRole(replyProfile),
                            fontSize = 10.sp,
                            color = when {
                                replyProfile?.verified != true -> Color.Gray
                                replyProfile?.registeredAs == "Administrator" -> Color.Red
                                replyProfile?.registeredAs == "Expert" -> Color.Blue
                                else -> Color.Gray
                            }
                        )
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

                Text(reply.reply_body ?: "", fontSize = 14.sp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onToggleLike(reply.id) }
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$likeCount",
                            fontSize = 12.sp,
                            color = if (isLiked) Color.Red else Color.Gray
                        )
                    }

                    Text(
                        text = "Reply",
                        color = Color(0xFF6200EE),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clickable { onReplyClick(reply.id) }
                            .padding(4.dp)
                    )
                }
            }
        }

        HorizontalDivider(
            color = Brown1.copy(alpha = 0.2f),
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (nestedReplies.isNotEmpty() && !showNestedReplies) {
            Text(
                text = "View ${nestedReplies.size} replies",
                color = Brown1,
                modifier = Modifier
                    .clickable { showNestedReplies = true }
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        if (showNestedReplies) {
            nestedReplies.forEach { childReply ->
                ReplyThread(
                    reply = childReply,
                    replies = replies,
                    onReplyClick = onReplyClick,
                    onUnsendReply = onUnsendReply,
                    onToggleLike = onToggleLike,
                    indent = indent + 1,
                    profiles = profiles,
                    currentUserId = currentUserId,
                    viewModel = viewModel,
                    onProfileClick = onProfileClick
                )
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
    }
}
