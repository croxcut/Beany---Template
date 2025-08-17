package com.example.feature.community.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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

@Composable
fun PostsListPage(
    viewModel: PostsViewModel = hiltViewModel(),
    onPostClick: (Long) -> Unit
) {
    val posts by remember { derivedStateOf { viewModel.sortedPosts } }
    var expanded by remember { mutableStateOf(false) }
    var showNewPostDialog by remember { mutableStateOf(false) }
    var newPostTitle by remember { mutableStateOf("") }
    var newPostBody by remember { mutableStateOf("") }

    val profile by viewModel.profiles.collectAsState()
    val session by viewModel.profile.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Brown1
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                )
            }

            Text(
                text = "COMMUNITY",
                style = TextStyle(
                    fontFamily = GlacialIndifferenceBold,
                    fontSize = rspSp(25.sp),
                    color = White,
                    letterSpacing = rspSp(5.sp)
                ),
            )

            // 🔍 Search Bar
            TextField(
                value = viewModel.searchQuery.value,
                onValueChange = { viewModel.setSearchQuery(it) },
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

            // Posts list (filtered)
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(viewModel.filteredPosts) { post ->
                    val postProfile = profile.find { it.id == post.sender }
                    PostItem(
                        post = post,
                        onPostClick = onPostClick,
                        onDeleteClick = { viewModel.setPostToDelete(it) },
                        profile = postProfile,
                        currentUserId = session?.id.toString()
                    )
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

        // Delete post dialog
        viewModel.postToDelete.value?.let { post ->
            DeletePostDialog(
                onDismiss = { viewModel.setPostToDelete(null) },
                onConfirm = { viewModel.deletePost(post.id) }
            )
        }

        // New post dialog
        if (showNewPostDialog) {
            AlertDialog(
                onDismissRequest = { showNewPostDialog = false },
                title = { Text("Create New Post") },
                text = {
                    Column {
                        TextField(
                            value = newPostTitle,
                            onValueChange = { newPostTitle = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Title") },
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = newPostBody,
                            onValueChange = { newPostBody = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Content") },
                            maxLines = 5
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newPostTitle.isNotBlank() && newPostBody.isNotBlank()) {
                                viewModel.createPost(newPostTitle, newPostBody)
                                newPostTitle = ""
                                newPostBody = ""
                                showNewPostDialog = false
                            }
                        }
                    ) {
                        Text("Post")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showNewPostDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
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
    currentUserId: String
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onPostClick(post.id) },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val displayName = profile?.username ?: "Unknown"

                Text(
                    text = displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                // Show menu only for the current user's posts
                if (post.sender == currentUserId) {
                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.size(32.dp) // make the whole button smaller
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit, // horizontal 3 dots
                                contentDescription = "More Options",
                                modifier = Modifier.size(18.dp) // make just the icon smaller
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    // TODO: handle edit
                                    menuExpanded = false
                                }
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

            Spacer(modifier = Modifier.height(4.dp))

            if (!post.post_title.isNullOrBlank()) {
                Text(
                    text = post.post_title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            if (!post.post_body.isNullOrBlank()) {
                Text(
                    text = post.post_body.toString(),
                    fontSize = 14.sp
                )
            }
        }
    }
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