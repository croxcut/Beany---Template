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

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.domain.model.supabase.Post
import com.example.domain.model.supabase.Profile
import com.example.domain.model.supabase.Reply
import com.example.feature.R
import com.example.feature.community.misc.formatUserRole
import com.example.feature.community.viewModels.PostDetailViewModel

@Composable
fun PostCard(
    post: Post,
    replies: List<Reply>,
    onReplyClick: (Long) -> Unit,
    onUnsendReply: (Long) -> Unit,
    onToggleLike: (Long) -> Unit,
    profiles: List<Profile>,
    sessionId: String,
    viewModel: PostDetailViewModel,
    onProfileClick: (Profile) -> Unit,
    onImageZoom: (Uri?) -> Unit
) {
    val postProfile = profiles.find { it.id == post.sender }
    val context = LocalContext.current

    val postImageUri by viewModel.getPostImageUri(post.image_url).collectAsState(initial = null)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .padding(rspDp(5.dp))
                .fillMaxWidth()
                .background(
                    color = White,
                    shape = RoundedCornerShape(rspDp(10.dp))
                )
                .padding(rspDp(15.dp))
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        postProfile?.let { onProfileClick(it) }
                    }
            ) {
                val profileImageUri by viewModel.getProfileImageUri(postProfile?.id).collectAsState()

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(profileImageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Picture",
                    placeholder = painterResource(R.drawable.plchldr),
                    error = painterResource(R.drawable.plchldr),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, Brown1, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = postProfile?.username ?: "Unknown",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatUserRole(postProfile),
                        fontSize = 12.sp,
                        color = when {
                            postProfile?.verified != true -> Color.Gray
                            postProfile?.registeredAs == "Administrator" -> Color.Red
                            postProfile?.registeredAs == "Expert" -> Color.Blue
                            else -> Color.Gray
                        }
                    )
                }
            }

            Spacer(Modifier.height(4.dp))
            Text(post.post_title, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(post.post_body ?: "")
            Spacer(Modifier.height(8.dp))

            postImageUri?.let { uri ->
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(uri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Post image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onImageZoom(uri) },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            HorizontalDivider(
                color = Brown1.copy(alpha = 0.2f),
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Replies",
                    color = Brown1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }

            replies.filter { it.parent_reply_id == null }.forEach { reply ->
                ReplyThread(
                    reply = reply,
                    replies = replies,
                    onReplyClick = onReplyClick,
                    onUnsendReply = onUnsendReply,
                    onToggleLike = onToggleLike,
                    indent = 0,
                    profiles = profiles,
                    currentUserId = sessionId,
                    viewModel = viewModel,
                    onProfileClick = onProfileClick
                )
            }
        }
    }
}