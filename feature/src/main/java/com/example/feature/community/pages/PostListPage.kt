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
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.domain.model.supabase.Post
import com.example.domain.model.supabase.Profile
import com.example.feature.community.viewModels.PostsViewModel
import com.example.feature.R
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.window.Dialog
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.composables.InputField
import com.example.domain.model.supabase.Reply
import com.example.feature.community.misc.DeletePostDialog
import com.example.feature.community.misc.NewPostDialog
import com.example.feature.community.misc.PostItem
import com.example.feature.community.misc.ProfileDialog
import com.example.feature.community.misc.RandomReplyItem
import com.example.feature.community.misc.ZoomableImageDialog
import kotlinx.coroutines.delay
import com.example.feature.community.misc.ZoomableImageState
import com.example.feature.community.misc.formatDate

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

    var zoomState by remember { mutableStateOf(ZoomableImageState()) }

    var tagsDropdownExpanded by remember { mutableStateOf(false) }

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

        ZoomableImageDialog(
            imageUri = zoomState.imageUri,
            onDismiss = { zoomState = ZoomableImageState() }
        )

        Column(
            modifier = Modifier.fillMaxSize(),
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
                                    viewModel = viewModel,
                                    onImageZoom = { uri ->
                                        zoomState = ZoomableImageState(isVisible = true, imageUri = uri)
                                    }
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

        viewModel.postToDelete.value?.let { post ->
            DeletePostDialog(
                onDismiss = { viewModel.setPostToDelete(null) },
                onConfirm = { viewModel.deletePost(post.id) }
            )
        }

        val selectedImage by viewModel.selectedImageUri

        if (showNewPostDialog) {
            NewPostDialog(
                onDismiss = {
                    showNewPostDialog = false
                    viewModel.clearSelectedImage()
                },
                onPost = { title, body, tags, imageUri ->
                    viewModel.createPost(title, body, tags, imageUri)
                    newPostTitle = ""
                    newPostBody = ""
                    viewModel.setSelectedTags(emptyList())
                    showNewPostDialog = false
                },
                availableTags = availableTags,
                selectedTags = selectedTags,
                onTagsChanged = { viewModel.setSelectedTags(it) },
                title = newPostTitle,
                onTitleChanged = { newPostTitle = it },
                body = newPostBody,
                onBodyChanged = { newPostBody = it },
                selectedImage = selectedImage,
                onImageSelected = { viewModel.setSelectedImage(it) }
            )
        }
    }
}

