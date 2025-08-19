package com.example.feature.community.pages

import android.net.Uri
import android.util.Log
import android.view.Menu
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Post
import com.example.domain.model.Profile
import com.example.feature.community.viewModels.PostsViewModel
import com.example.feature.R
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.domain.model.Reply
import kotlinx.coroutines.delay

@Composable
fun PostsListPage(
    viewModel: PostsViewModel = hiltViewModel(),
    onPostClick: (Long) -> Unit
) {
    var showNewPostDialog by remember { mutableStateOf(false) }
    var newPostTitle by remember { mutableStateOf("") }
    var newPostBody by remember { mutableStateOf("") }
    val availableTags = remember { listOf("Disease", "Questions", "Tips") }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()
    val posts = viewModel.postsPagingFlow.collectAsLazyPagingItems()
    val profile by viewModel.profiles.collectAsState(initial = emptyList())
    val session by viewModel.profile.collectAsState()


    var tagsDropdownExpanded by remember { mutableStateOf(false) }

    // Debounced search
    var localSearchQuery by remember { mutableStateOf("") }
    LaunchedEffect(localSearchQuery) {
        delay(300)
        viewModel.setSearchQuery(localSearchQuery)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Brown1)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = rspDp(20.dp))
            ) {
                Text(
                    text = "BEANY",
                    style = TextStyle(
                        fontFamily = Kare,
                        fontSize = rspSp(25.sp),
                        color = White
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Text(
                text = "COMMUNITY",
                style = TextStyle(
                    fontFamily = GlacialIndifferenceBold,
                    fontSize = rspSp(25.sp),
                    color = White,
                    letterSpacing = rspSp(5.sp)
                )
            )

            // Search Bar
            TextField(
                value = localSearchQuery,
                onValueChange = { localSearchQuery = it },
                textStyle = TextStyle(
                    color = Brown1,
                    fontFamily = Etna,
                    fontSize = rspSp(20.sp)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        color = White,
                        shape = RoundedCornerShape(rspDp(10.dp))
                    ),
                placeholder = { Text("Search posts...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Brown1,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            // Tags Section
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { tagsDropdownExpanded = true },
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            color = Beige1,
                            shape = RoundedCornerShape(rspDp(10.dp))
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text("Filter by Tags ${selectedTags.size}")

                    DropdownMenu(
                        expanded = tagsDropdownExpanded,
                        onDismissRequest = { tagsDropdownExpanded = false },
                        containerColor = Beige1
                    ) {
                        availableTags.forEach { tag ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = selectedTags.contains(tag),
                                            onCheckedChange = null,
                                        )
                                        Text(
                                            text = tag,
                                            style = TextStyle(
                                                fontFamily = GlacialIndifferenceBold,
                                                fontSize = rspSp(15.sp),
                                                color = Brown1
                                            )
                                        )
                                    }
                                },
                                onClick = {
                                    viewModel.setSelectedTags(
                                        if (selectedTags.contains(tag)) {
                                            selectedTags - tag
                                        } else {
                                            selectedTags + tag
                                        }
                                    )
                                },
                                modifier = Modifier.background(Beige1),
                            )
                        }
                    }
                }


            }

            if (selectedTags.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    selectedTags.forEach { tag ->
                        FilterChip(
                            selected = true,
                            onClick = {
                                viewModel.setSelectedTags(selectedTags - tag)
                            },
                            label = { Text(tag) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remove tag"
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Beige1,
                                selectedLabelColor = Brown1
                            )
                        )
                    }
                }
            }

            // Posts List
            when (posts.loadState.refresh) {
                is LoadState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is LoadState.Error -> {
                    val error = (posts.loadState.refresh as LoadState.Error).error
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${error.localizedMessage}", color = Color.Red)
                        Log.i("Posts-Page:" , "$error")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(
                            count = posts.itemCount,
                            key = { index -> posts[index]?.id ?: index }
                        ) { index ->
                            posts[index]?.let { post ->
                                val postProfile = remember(post.sender) {
                                    profile.find { it.id == post.sender }
                                }
                                PostItem(
                                    post = post,
                                    onPostClick = onPostClick,
                                    onDeleteClick = { viewModel.setPostToDelete(it) },
                                    profile = postProfile,
                                    currentUserId = session?.id.toString(),
                                    viewModel = viewModel
                                )
                            }
                        }

                        if (posts.loadState.append is LoadState.Loading) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showNewPostDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Beige1,
            contentColor = Brown1
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add new post")
        }

        // Dialogs
        viewModel.postToDelete.value?.let { post ->
            DeletePostDialog(
                onDismiss = { viewModel.setPostToDelete(null) },
                onConfirm = { viewModel.deletePost(post.id) }
            )
        }

        if (showNewPostDialog) {
            NewPostDialog(
                onDismiss = { showNewPostDialog = false },
                onPost = { title, body, tags ->
                    viewModel.createPost(title, body, tags)
                    newPostTitle = ""
                    newPostBody = ""
                    viewModel.setSelectedTags(emptyList()) // Update via ViewModel
                    showNewPostDialog = false
                },
                availableTags = availableTags,
                selectedTags = selectedTags,
                onTagsChanged = { viewModel.setSelectedTags(it) }, // Update via ViewModel
                title = newPostTitle,
                onTitleChanged = { newPostTitle = it },
                body = newPostBody,
                onBodyChanged = { newPostBody = it }
            )
        }
    }
}

@Composable
private fun PostItem(
    post: Post,
    onPostClick: (Long) -> Unit,
    onDeleteClick: (Post) -> Unit,
    profile: Profile?,
    currentUserId: String,
    viewModel: PostsViewModel
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
            // Header with profile info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showProfileDialog = true }
            ) {
                // Optimized profile image loading
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

            // Tags
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

            // Like and comment counters
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                // Like button
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

                // Comment count
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

            // Reply button
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

            // Random reply preview
            Divider(
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

@Composable
private fun RandomReplyItem(
    reply: Reply,
    profile: Profile?,
    currentUserId: String,
    viewModel: PostsViewModel
) {
    val profileImageUri by viewModel.getProfileImageUri(profile?.id).collectAsState(initial = null)
    val context = LocalContext.current
    val replyLikes by remember(reply.id) { derivedStateOf {
        reply.likes ?: emptyList()
    }}
    val isLiked = remember(replyLikes, currentUserId) {
        replyLikes.contains(currentUserId)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = White.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
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
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.dp, Brown1, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile?.username ?: "Unknown",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )

                    reply.created_at?.let { date ->
                        Text(
                            text = formatDate(date),
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        viewModel.toggleReplyLike(reply.id, currentUserId)
                    }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${replyLikes.size}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reply.reply_body ?: "",
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun ProfileDialog(
    profile: Profile,
    imageUri: Uri?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Profile Info",
                style = TextStyle(
                    fontFamily = Etna,
                    fontSize = rspSp(20.sp),
                    color = Brown1
                )
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Profile Picture",
                    placeholder = painterResource(R.drawable.plchldr),
                    error = painterResource(R.drawable.plchldr),
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Brown1, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text(
                        text = "Username: ",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp),
                            color = Brown1
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    profile.username?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(18.sp),
                                color = Brown1
                            )
                        )
                    }
                }

                Row {
                    Text(
                        text = "Name: ",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp),
                            color = Brown1
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    profile.fullName?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(18.sp),
                                color = Brown1
                            )
                        )
                    }
                }

                Row {
                    Text(
                        text = "Registered: ",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp),
                            color = Brown1
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    profile.registeredAs?.let { registeredAs ->
                        val status = if (registeredAs == "Administrator") {
                            ""
                        } else {
                            if (profile.verified == true) "[verified]" else "[pending]"
                        }
                        Text(
                            text = "$registeredAs$status",
                            style = TextStyle(
                                fontSize = rspSp(18.sp),
                                color = when {
                                    profile.verified != true -> Color.Gray
                                    registeredAs == "Administrator" -> Color.Red
                                    registeredAs == "Expert" -> Color.Blue
                                    else -> Color.Gray
                                },
                                fontFamily = GlacialIndifferenceBold
                            )
                        )
                    }
                }


                profile.province?.takeIf { profile.registeredAs != "Administrator" }?.let { province ->
                    Row {
                        Text(
                            text = "Province: ",
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(18.sp),
                                color = Brown1
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = province,
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(18.sp),
                                color = Brown1
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Close",
                    style = TextStyle(
                        fontFamily = Etna,
                        fontSize = rspSp(18.sp),
                        color = Brown1
                    )
                )
            }
        },
        containerColor = Beige1
    )
}

@Composable
private fun NewPostDialog(
    onDismiss: () -> Unit,
    onPost: (String, String, List<String>) -> Unit,
    availableTags: List<String>,
    selectedTags: List<String>,
    onTagsChanged: (List<String>) -> Unit,
    title: String,
    onTitleChanged: (String) -> Unit,
    body: String,
    onBodyChanged: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Post") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = onTitleChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Tags:", modifier = Modifier.padding(bottom = 4.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    availableTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                onTagsChanged(if (isSelected) {
                                    selectedTags - tag
                                } else {
                                    selectedTags + tag
                                })
                            },
                            label = { Text(tag) },
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = body,
                    onValueChange = onBodyChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Content") },
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank() && body.isNotBlank()) {
                        onPost(title, body, selectedTags)
                    }
                }
            ) {
                Text("Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun DeletePostDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Post") },
        text = { Text("Are you sure you want to delete this post?") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Yes") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("No") }
        }
    )
}

private fun formatDate(dateString: String): String {
    return try {
        val parts = dateString.split("T")[0].split("-")
        "${parts[1]}/${parts[2]}/${parts[0]}"
    } catch (e: Exception) {
        dateString
    }
}