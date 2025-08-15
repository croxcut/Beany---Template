package com.example.feature.community.pages

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.Post
import com.example.feature.community.viewModels.PostsViewModel

@Composable
fun PostsListPage(
    viewModel: PostsViewModel = hiltViewModel(),
    onPostClick: (Long) -> Unit
) {
    val posts by remember { derivedStateOf { viewModel.sortedPosts } }
    var newPostTitle by remember { mutableStateOf("") }
    var newPostBody by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {
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

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(posts) { post ->
                PostItem(post, onPostClick, onDeleteClick = {
                        viewModel.setPostToDelete(it)
                    }
                )
            }
        }

        TextField(
            value = newPostTitle,
            onValueChange = { newPostTitle = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            placeholder = { Text("Post title...") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newPostBody,
                onValueChange = { newPostBody = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("Write a new post...") },
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Button(
                onClick = {
                    viewModel.createPost(newPostTitle, newPostBody)
                    newPostTitle = ""
                    newPostBody = ""
                },
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Post")
            }
        }
    }

    viewModel.postToDelete.value?.let { post ->
        DeletePostDialog(
            onDismiss = { viewModel.setPostToDelete(null) },
            onConfirm = { viewModel.deletePost(post.id) }
        )
    }
}

@Composable
private fun PostItem(
    post: Post,
    onPostClick: (Long) -> Unit,
    onDeleteClick: (Post) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onPostClick(post.id) }
            ) {
                Text(post.sender, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(post.post_body ?: "")
            }
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete Post",
                tint = Color.Red,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDeleteClick(post) }
            )
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