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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.core.net.toUri
import com.example.domain.model.supabase.NewReply
import com.example.domain.model.supabase.Post
import com.example.domain.model.supabase.Profile
import com.example.domain.model.supabase.Reply
import com.example.domain.repository.remote.supabase.BucketRepository
import com.example.domain.repository.remote.supabase.PostRepository
import com.example.domain.repository.remote.supabase.ReplyRepository
import com.example.domain.repository.remote.supabase.SessionRepository
import com.example.domain.repository.remote.supabase.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val replyRepository: ReplyRepository,
    private val userRepository: UsersRepository,
    private val sessionRepository: SessionRepository,
    private val bucketRepository: BucketRepository,
    private val context: Context
) : ViewModel() {
    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    private val _postImageUris = mutableStateMapOf<String, Uri?>()
    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())
    val profiles: StateFlow<List<Profile>> = _profiles
    private val _post = mutableStateOf<Post?>(null)
    val post: State<Post?> = _post

    private val _replies = mutableStateListOf<Reply>()
    val replies: List<Reply> = _replies

    private val _parentReplyId = mutableStateOf<Long?>(null)
    val parentReplyId: State<Long?> = _parentReplyId

    private val _profileImageUris = mutableStateMapOf<String, Uri?>()
    private var repliesJob: Job? = null

    fun loadPost(postId: Long) {
        viewModelScope.launch {
            try {
                _post.value = postRepository.getPostById(postId)
            } catch (e: Exception) {
                println("Failed to fetch post: ${e.message}")
            }
        }
    }

    fun loadProfiles() {
        viewModelScope.launch {
            _profiles.value = userRepository.getProfiles()
        }
    }

    fun loadSession() {
        viewModelScope.launch {
            _profile.value = sessionRepository.getUserProfile()
        }
    }

    fun loadReplies(postId: Long) {
        repliesJob?.cancel()

        repliesJob = viewModelScope.launch {
            try {
                replyRepository.getRepliesFlow(postId).collect { replyList ->
                    _replies.clear()
                    _replies.addAll(replyList)
                }
            } catch (e: Exception) {
                println("Failed to fetch replies: ${e.message}")
            }
        }
    }

    fun toggleReplyLike(replyId: Long) {
        viewModelScope.launch {
            try {
                profile.value?.id?.let { userId ->
                    replyRepository.toggleReplyLike(replyId, userId)
                }
            } catch (e: Exception) {
                println("Error toggling reply like: ${e.message}")
            }
        }
    }

    fun clearParentReply() {
        _parentReplyId.value = null
    }

    fun createReply(postId: Long, body: String) {
        if (body.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val reply = NewReply(
                        post_id = postId,
                        sender = profile.value?.id.toString(),
                        reply_body = body,
                        parent_reply_id = _parentReplyId.value
                    )
                    replyRepository.createReply(reply)
                    _parentReplyId.value = null
                } catch (e: Exception) {
                    println("Error creating reply: ${e.message}")
                }
            }
        }
    }

    fun setParentReplyId(replyId: Long?) {
        _parentReplyId.value = replyId
    }

    fun unsendReply(replyId: Long) {
        viewModelScope.launch {
            try {
                replyRepository.unsendReply(replyId)
            } catch (e: Exception) {
                println("Error unsending reply: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        repliesJob?.cancel()
        super.onCleared()
    }

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

    fun getPostImageUri(imageUrl: String?): StateFlow<Uri?> {
        val flow = MutableStateFlow<Uri?>(null)
        if (imageUrl.isNullOrBlank()) return flow.asStateFlow()

        viewModelScope.launch {
            try {
                if (!_postImageUris.containsKey(imageUrl)) {
                    // For post images, the remote path should be the imageUrl directly
                    // since it already contains the full path like "posts/12345.jpg"
                    val remotePath = "posts/$imageUrl"
                    println("DEBUG: Fetching post image from: $remotePath")

                    val imageBytes = bucketRepository.getImage(remotePath)
                    val tempFile = File(context.cacheDir, "${imageUrl}.jpg")
                    tempFile.writeBytes(imageBytes)
                    _postImageUris[imageUrl] = tempFile.toUri()
                    println("DEBUG: Successfully cached post image: ${tempFile.toUri()}")
                }
                flow.value = _postImageUris[imageUrl]
            } catch (e: Exception) {
                println("Error getting post image: ${e.message}")
                flow.value = null
            }
        }

        return flow.asStateFlow()
    }

    fun clearPostImageCache() {
        _postImageUris.clear()
    }

}