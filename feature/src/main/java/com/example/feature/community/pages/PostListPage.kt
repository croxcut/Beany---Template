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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
            .background(Color(0xFFF0F0F0))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Sorting dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .clickable { expanded = true }
                    .padding(12.dp)
            ) {
                Text("Sort by: ${viewModel.sortOption.value}")
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Date") },
                        onClick = {
                            viewModel.setSortOption("Date")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Name") },
                        onClick = {
                            viewModel.setSortOption("Name")
                            expanded = false
                        }
                    )
                }
            }

            // Posts list
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(posts) { post ->
                    val postProfile = profile.find { it.id == post.sender }
                    profile.let {
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
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showNewPostDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
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
    profile: Profile?,      // Current logged-in user's profile
    currentUserId: String     // Pass the current logged-in user's ID
) {
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

                // Only show delete icon if the current user is the sender
                if (post.sender == currentUserId) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete Post",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onDeleteClick(post) }
                    )
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