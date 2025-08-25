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

package com.example.feature.community.viewModels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.repositoryImpl.remote.supabase.db.PostsPagingSource
import com.example.domain.model.supabase.NewPost
import com.example.domain.model.supabase.Post
import com.example.domain.model.supabase.Profile
import com.example.domain.model.supabase.Reply
import com.example.domain.repository.remote.supabase.BucketRepository
import com.example.domain.repository.remote.supabase.PostRepository
import com.example.domain.repository.remote.supabase.ReplyRepository
import com.example.domain.repository.remote.supabase.SessionRepository
import com.example.domain.repository.remote.supabase.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UsersRepository,
    private val sessionRepository: SessionRepository,
    private val bucketRepository: BucketRepository,
    private val replyRepository: ReplyRepository,
    private val context: Context
) : ViewModel() {
    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())
    val profiles: StateFlow<List<Profile>> = _profiles
    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> get() = _posts


    private val _postToDelete = mutableStateOf<Post?>(null)
    val postToDelete: State<Post?> = _postToDelete

    private val _postLikes = mutableStateMapOf<Long, List<String>>()
    val postLikes: SnapshotStateMap<Long, List<String>> = _postLikes
    private val _topReplies = mutableStateMapOf<Long, Reply?>()

    init {
        loadPosts()
        loadSession()
        loadProfiles()
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTags = MutableStateFlow<List<String>>(emptyList())
    val selectedTags: StateFlow<List<String>> = _selectedTags.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(0)

    val postsPagingFlow: Flow<PagingData<Post>> = combine(
        searchQuery,
        selectedTags,
        _refreshTrigger
    ) { query, tags, _ ->
        Pair(query, tags)
    }.flatMapLatest { (query, tags) ->
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                PostsPagingSource(
                    postRepository = postRepository,
                    searchQuery = query.takeIf { it.isNotBlank() },
                    selectedTags = tags
                )
            }
        ).flow
    }.cachedIn(viewModelScope)

    fun refreshPosts() {
        _refreshTrigger.value++
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedTags(tags: List<String>) {
        _selectedTags.value = tags
    }

    fun loadProfiles() {
        viewModelScope.launch {
            _profiles.value = userRepository.getProfiles()
        }
    }
    private fun loadSession() {
        viewModelScope.launch {
            _profile.value = sessionRepository.getUserProfile()
        }
    }

    private val _profileImageUris = mutableStateMapOf<String, Uri?>()

    fun getProfileImageUri(userId: String?): StateFlow<Uri?> {
        val flow = MutableStateFlow<Uri?>(null)
        if (userId == null) return flow.asStateFlow()

        viewModelScope.launch {
            try {
                if (!_profileImageUris.containsKey(userId)) {
                    val remotePath = "profiles/${userId}.jpg"
                    val imageBytes = bucketRepository.getImage(remotePath)
                    val tempFile = File(context.cacheDir, "profile_${userId}.jpg")
                    tempFile.writeBytes(imageBytes)
                    _profileImageUris[userId] = tempFile.toUri()
                }
                flow.value = _profileImageUris[userId]
            } catch (e: Exception) {
                flow.value = null
            }
        }

        return flow.asStateFlow()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            try {
                _posts.clear()
                val loadedPosts = postRepository.getPosts()
                _posts.addAll(loadedPosts)

                loadedPosts.forEach { post ->
                    loadReplyCount(post.id)
                }
            } catch (e: Exception) {
                println("Error loading posts: ${e.message}")
            }
        }
    }

    fun createPost(title: String, body: String, tags: List<String> = emptyList()) {
        if (title.isNotBlank() && body.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val newPost = NewPost(
                        sender = profile.value?.id.toString(),
                        post_title = title,
                        post_body = body,
                        tags = tags.takeIf { it.isNotEmpty() }
                    )
                    postRepository.createPost(newPost)
                    loadPosts()
                } catch (e: Exception) {
                    println("Error creating post: ${e.message}")
                }
            }
        }
    }

    fun setPostToDelete(post: Post?) {
        _postToDelete.value = post
    }

    fun deletePost(postId: Long) {
        viewModelScope.launch {
            try {
                postRepository.deletePost(postId)
                _posts.removeIf { it.id == postId }
                refreshPosts()
            } catch (e: Exception) {
                println("Error deleting post: ${e.message}")
            } finally {
                _postToDelete.value = null
            }
        }
    }

    fun toggleLike(postId: Long, userId: String) {
        viewModelScope.launch {
            try {
                val currentLikes = _postLikes[postId]?.toMutableList() ?:
                postRepository.getPostById(postId)?.likes?.toMutableList() ?: mutableListOf()

                if (currentLikes.contains(userId)) {
                    currentLikes.remove(userId)
                } else {
                    currentLikes.add(userId)
                }

                _postLikes[postId] = currentLikes

                postRepository.updatePostLikes(postId, currentLikes)
            } catch (e: Exception) {
                println("Error toggling like: ${e.message}")
            }
        }
    }
    private val _replyCounts = mutableStateMapOf<Long, Int>()

    fun loadReplyCount(postId: Long) {
        viewModelScope.launch {
            try {
                val count = postRepository.getCommentCount(postId)
                _replyCounts[postId] = count
            } catch (e: Exception) {
                println("Error loading reply count: ${e.message}")
            }
        }
    }

    fun getReplyCount(postId: Long): Int {
        return _replyCounts[postId] ?: 0
    }

    fun getTopReply(postId: Long): Flow<Reply?> {
        return flow {
            try {
                val replies = replyRepository.getRepliesForPost(postId)
                val topReply = replies.maxByOrNull { it.likes?.size ?: 0 }
                _topReplies[postId] = topReply
                emit(topReply)
            } catch (e: Exception) {
                println("Error getting top reply: ${e.message}")
                emit(null)
            }
        }
    }

    fun toggleReplyLike(replyId: Long, userId: String) {
        viewModelScope.launch {
            try {
                replyRepository.toggleReplyLike(replyId, userId)

                val postId = _topReplies.entries.find { it.value?.id == replyId }?.key
                postId?.let { getTopReply(it).collect() }
            } catch (e: Exception) {
                println("Error toggling reply like: ${e.message}")
            }
        }
    }

    fun getRandomReply(postId: Long): Flow<Reply?> {
        return flow {
            try {
                val replies = replyRepository.getRepliesForPost(postId)
                val randomReply = if (replies.isNotEmpty()) replies.random() else null
                _topReplies[postId] = randomReply
                emit(randomReply)
            } catch (e: Exception) {
                println("Error getting random reply: ${e.message}")
                emit(null)
            }
        }
    }

    private val _selectedImageUri = mutableStateOf<Uri?>(null)
    val selectedImageUri: State<Uri?> = _selectedImageUri

    suspend fun uploadPostImage(uri: Uri): String? {
        return try {
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: return null

            val filename = "post_${System.currentTimeMillis()}.jpg"
            val remotePath = "posts/$filename"

            bucketRepository.upload(remotePath, bytes)
            filename
        } catch (e: Exception) {
            println("Error uploading post image: ${e.message}")
            null
        }
    }

    fun setSelectedImage(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun clearSelectedImage() {
        _selectedImageUri.value = null
    }

    fun createPost(title: String, body: String, tags: List<String> = emptyList(), imageUri: Uri? = null) {
        if (title.isNotBlank() && body.isNotBlank()) {
            viewModelScope.launch {
                try {
                    var imageFilename: String? = null

                    imageUri?.let { uri ->
                        imageFilename = uploadPostImage(uri)
                    }

                    val newPost = NewPost(
                        sender = profile.value?.id.toString(),
                        post_title = title,
                        post_body = body,
                        image_url = imageFilename,
                        tags = tags.takeIf { it.isNotEmpty() }
                    )

                    postRepository.createPost(newPost)
                    clearSelectedImage()

                    refreshPosts()

                } catch (e: Exception) {
                    println("Error creating post: ${e.message}")
                }
            }
        }
    }

    fun getPostImageUri(imageUrl: String?): StateFlow<Uri?> {
        val flow = MutableStateFlow<Uri?>(null)

        if (imageUrl == null) return flow.asStateFlow()

        viewModelScope.launch {
            try {
                val remotePath = "posts/$imageUrl"
                val imageBytes = bucketRepository.getImage(remotePath)
                val tempFile = File(context.cacheDir, "post_$imageUrl")
                tempFile.writeBytes(imageBytes)
                flow.value = tempFile.toUri()
            } catch (e: Exception) {
                flow.value = null
            }
        }

        return flow.asStateFlow()
    }

}