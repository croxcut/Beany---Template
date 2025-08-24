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

package com.example.feature.community.misc

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.supabase.Post
import com.example.domain.model.supabase.Profile
import com.example.domain.model.supabase.Reply
import com.example.feature.R
import com.example.feature.community.viewModels.PostsViewModel

@Composable
fun PostItem(
    post: Post,
    onPostClick: (Long) -> Unit,
    onDeleteClick: (Post) -> Unit,
    profile: Profile?,
    currentUserId: String,
    viewModel: PostsViewModel,
    onImageZoom: (Uri?) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var showProfileDialog by remember { mutableStateOf(false) }

    val postLikes by remember(post.id) { derivedStateOf {
        viewModel.postLikes[post.id] ?: post.likes ?: emptyList()
    }}
    val isLiked = remember(postLikes, currentUserId) {
        postLikes.contains(currentUserId)
    }

    val replyCount by remember(post.id) {
        derivedStateOf { viewModel.getReplyCount(post.id) }
    }

    var randomReply by remember { mutableStateOf<Reply?>(null) }

    val profileImageUri by viewModel.getProfileImageUri(profile?.id).collectAsState(initial = null)

    val postImageUri by viewModel.getPostImageUri(post.image_url).collectAsState(initial = null)
    var showZoomableImage by remember { mutableStateOf(false) }

    if (showZoomableImage) {
        ZoomableImageDialog(
            imageUri = postImageUri,
            onDismiss = { showZoomableImage = false }
        )
    }
    LaunchedEffect(post.id) {
        viewModel.getRandomReply(post.id).collect {
            randomReply = it
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Beige1)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showProfileDialog = true }
            ) {
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
                        text = profile?.username ?: "Unknown",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(15.sp),
                            color = Brown1
                        )
                    )

                    Row {
                        profile?.registeredAs?.let { registeredAs ->
                            val status = if (registeredAs == "Administrator") {
                                ""
                            } else {
                                if (profile.verified == true) "[verified]" else "[pending]"
                            }

                            Text(
                                text = "$registeredAs$status",
                                fontSize = rspSp(12.sp),
                                color = when {
                                    profile.verified != true -> Color.Gray
                                    registeredAs == "Administrator" -> Color.Red
                                    registeredAs == "Expert" -> Color.Blue
                                    else -> Color.Gray
                                }
                            )
                        }
                        profile?.province?.takeIf { profile?.registeredAs != "Administrator" }?.let { province ->
                            Text(
                                text = " | $province",
                                fontSize = rspSp(12.sp),
                                color = Color.Gray
                            )
                        }
                    }
                }

                if (post.sender == currentUserId) {
                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "More Options",
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = { menuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    onDeleteClick(post)
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatDate(post.created_at),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            post.post_title?.let { title ->
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            post.tags?.takeIf { it.isNotEmpty() }?.let { tags ->
                FlowRow(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    color = Brown1,
                                    shape = RoundedCornerShape(rspDp(4.dp))
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = tag,
                                style = TextStyle(
                                    fontSize = rspSp(12.sp),
                                    color = White,
                                    fontFamily = GlacialIndifference
                                )
                            )
                        }
                    }
                }
            }

            post.post_body?.let { body ->
                Text(
                    text = body,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

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
                        .clickable { onImageZoom(uri) },  // Use the callback
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        viewModel.toggleLike(post.id, currentUserId)
                    }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${postLikes.size}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onPostClick(post.id) }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.comment),
                        contentDescription = "Comments",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$replyCount",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = "Reply",
                fontSize = 14.sp,
                color = Brown1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onPostClick(post.id) }
                    .padding(top = 8.dp)
            )

            HorizontalDivider(
                color = Brown1.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (replyCount > 0 && randomReply != null) {
                RandomReplyItem(
                    reply = randomReply!!,
                    profile = profile,
                    currentUserId = currentUserId,
                    viewModel = viewModel
                )
            } else {
                Text(
                    text = "No replies yet",
                    style = TextStyle(
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (showProfileDialog && profile != null) {
                ProfileDialog(
                    profile = profile,
                    imageUri = profileImageUri,
                    onDismiss = { showProfileDialog = false }
                )
            }

        }
    }
}