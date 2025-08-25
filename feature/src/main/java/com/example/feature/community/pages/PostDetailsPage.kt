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

package com.example.feature.community.pages

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.supabase.Post
import com.example.domain.model.supabase.Profile
import com.example.domain.model.supabase.Reply
import com.example.feature.community.viewModels.PostDetailViewModel
import com.example.feature.R
import com.example.feature.community.components.PostCard
import com.example.feature.community.components.ReplyThread
import com.example.feature.community.misc.ParentReplyInThread
import com.example.feature.community.misc.ParentReplyPreview
import com.example.feature.community.misc.ProfileDialog
import com.example.feature.community.misc.ReplyInput
import com.example.feature.community.misc.ZoomableImageDialog
import com.example.feature.community.misc.ZoomableImageState
import com.example.feature.community.misc.formatUserRole

@Composable
fun PostDetailPage(
    postId: Long,
    viewModel: PostDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val post by viewModel.post
    val replies by remember { derivedStateOf { viewModel.replies } }
    var newReply by remember { mutableStateOf("") }
    val profiles by viewModel.profiles.collectAsState()
    val session by viewModel.profile.collectAsState()

    var showProfileDialog by remember { mutableStateOf(false) }
    var selectedProfile by remember { mutableStateOf<Profile?>(null) }
    var selectedProfileImageUri by remember { mutableStateOf<Uri?>(null) }

    var zoomState by remember { mutableStateOf(ZoomableImageState()) }

    LaunchedEffect(selectedProfile) {
        selectedProfile?.id?.let { profileId ->
            viewModel.getProfileImageUri(profileId).collect { uri ->
                selectedProfileImageUri = uri
            }
        }
    }

    LaunchedEffect(postId) {
        viewModel.loadSession()
        viewModel.loadProfiles()
        viewModel.loadPost(postId)
        viewModel.loadReplies(postId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Brown1)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        ZoomableImageDialog(
            imageUri = zoomState.imageUri,
            onDismiss = { zoomState = ZoomableImageState() }
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Post Details",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    post?.let { postItem ->
                        PostCard(
                            post = postItem,
                            replies = replies,
                            onReplyClick = viewModel::setParentReplyId,
                            onUnsendReply = viewModel::unsendReply,
                            onToggleLike = viewModel::toggleReplyLike,
                            profiles = profiles,
                            sessionId = session?.id.toString(),
                            viewModel = viewModel,
                            onProfileClick = { profile ->
                                selectedProfile = profile
                                showProfileDialog = true
                            },
                            onImageZoom = { uri ->
                                zoomState = ZoomableImageState(isVisible = true, imageUri = uri)
                            }
                        )
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
                },
                onClearParent = { viewModel.clearParentReply() }
            )

            if (showProfileDialog && selectedProfile != null) {
                ProfileDialog(
                    profile = selectedProfile!!,
                    imageUri = selectedProfileImageUri,
                    onDismiss = { showProfileDialog = false }
                )
            }
        }
    }
}