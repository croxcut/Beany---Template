package com.example.feature.community.pages

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.domain.model.Post
import com.example.domain.model.Profile
import com.example.domain.model.Reply
import com.example.feature.community.viewModels.PostDetailViewModel
import com.example.feature.R
import com.example.feature.community.misc.ParentReplyInThread
import com.example.feature.community.misc.ParentReplyPreview
import com.example.feature.community.misc.ProfileDialog
import com.example.feature.community.misc.formatUserRole

@Composable
fun PostDetailPage(
    postId: Long,
    viewModel: PostDetailViewModel = hiltViewModel()
) {
    val post by viewModel.post
    val replies by remember { derivedStateOf { viewModel.replies } }
    var newReply by remember { mutableStateOf("") }
    val profiles by viewModel.profiles.collectAsState()
    val session by viewModel.profile.collectAsState()

    // Profile dialog state
    var showProfileDialog by remember { mutableStateOf(false) }
    var selectedProfile by remember { mutableStateOf<Profile?>(null) }
    var selectedProfileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Load profile image when profile is selected
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Brown1)
            .statusBarsPadding()
            .navigationBarsPadding()
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

        // Profile Dialog
        if (showProfileDialog && selectedProfile != null) {
            ProfileDialog(
                profile = selectedProfile!!,
                imageUri = selectedProfileImageUri,
                onDismiss = { showProfileDialog = false }
            )
        }
    }
}

@Composable
private fun PostCard(
    post: Post,
    replies: List<Reply>,
    onReplyClick: (Long) -> Unit,
    onUnsendReply: (Long) -> Unit,
    onToggleLike: (Long) -> Unit,
    profiles: List<Profile>,
    sessionId: String,
    viewModel: PostDetailViewModel,
    onProfileClick: (Profile) -> Unit
) {
    val postProfile = profiles.find { it.id == post.sender }
    val context = LocalContext.current

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
            // Profile Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        postProfile?.let { onProfileClick(it) }
                    }
            ) {
                // Profile Image
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

            // Reply button for the post
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Reply",
                    color = Brown1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onReplyClick(-1) } // Use -1 or null to indicate replying to post
                        .padding(top = 8.dp)
                )
            }

            // Direct replies to post (no indentation)
            replies.filter { it.parent_reply_id == null }.forEach { reply ->
                ReplyThread(
                    reply = reply,
                    replies = replies,
                    onReplyClick = onReplyClick,
                    onUnsendReply = onUnsendReply,
                    onToggleLike = onToggleLike,
                    indent = 0, // Start with 0 indentation for direct replies
                    profiles = profiles,
                    currentUserId = sessionId,
                    viewModel = viewModel,
                    onProfileClick = onProfileClick
                )
            }
        }
    }
}

@Composable
private fun ReplyThread(
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
    // Only indent up to maxVisualIndent levels
    val actualIndent = if (indent > 0 && indent <= maxVisualIndent) 16.dp else 0.dp
    var showDialog by remember { mutableStateOf(false) }
    val canUnsend = reply.reply_body != "Unsent a message" && reply.sender == currentUserId
    val isLiked = remember(reply.likes) { reply.likes?.contains(currentUserId) ?: false }
    val likeCount = remember(reply.likes) { reply.likes?.size ?: 0 }
    val replyProfile = profiles.find { it.id == reply.sender }
    val context = LocalContext.current
    val nestedReplies = replies.filter { it.parent_reply_id == reply.id }

    // Get the parent reply being referenced if this is a reply to another reply
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
        // Show parent reply preview if this is a reply to another reply
        parentReply?.let { parent ->
            ParentReplyInThread(
                parent = parent,
                profile = parentProfile,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // Main reply content
        Box(
            modifier = Modifier
                .background(
                    color = if (indent == 0) Color(0xFFD8EAFB) else Color(0xFFF1F0F0),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Column {
                // Profile row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            replyProfile?.let { onProfileClick(it) }
                        }
                ) {
                    // Profile image
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

                // Reply content
                Text(reply.reply_body ?: "", fontSize = 14.sp)

                // Like and reply actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Like button
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

                    // ALWAYS show reply button - no limit on reply depth
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

        // Show "View replies" button if there are nested replies
        if (nestedReplies.isNotEmpty() && !showNestedReplies) {
            Text(
                text = "View ${nestedReplies.size} replies",
                color = Brown1,
                modifier = Modifier
                    .clickable { showNestedReplies = true }
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        // Show nested replies when expanded
        if (showNestedReplies) {
            nestedReplies.forEach { childReply ->
                ReplyThread(
                    reply = childReply,
                    replies = replies,
                    onReplyClick = onReplyClick,
                    onUnsendReply = onUnsendReply,
                    onToggleLike = onToggleLike,
                    indent = indent + 1, // Keep incrementing indent but we'll cap the visual effect
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

@Composable
private fun ReplyInput(
    parentReplyId: Long?,
    replies: List<Reply>,
    profiles: List<Profile>,
    newReply: String,
    onNewReplyChange: (String) -> Unit,
    onSendReply: () -> Unit,
    onClearParent: () -> Unit // Add this parameter
) {
    Column {
        parentReplyId?.let { parentId ->
            replies.find { it.id == parentId }?.let { parent ->
                val parentProfile = profiles.find { it.id == parent.sender }
                ParentReplyPreview(
                    parent = parent,
                    profile = parentProfile,
                    onClose = onClearParent // Pass the close handler
                )
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




