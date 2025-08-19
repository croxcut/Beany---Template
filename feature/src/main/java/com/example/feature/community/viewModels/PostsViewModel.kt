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
import com.example.data.repositoryImpl.PostsPagingSource
import com.example.domain.model.NewPost
import com.example.domain.model.Post
import com.example.domain.model.Profile
import com.example.domain.model.Reply
import com.example.domain.repository.BucketRepository
import com.example.domain.repository.PostRepository
import com.example.domain.repository.ReplyRepository
import com.example.domain.repository.SessionRepository
import com.example.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _postToDelete = mutableStateOf<Post?>(null)
    val postToDelete: State<Post?> = _postToDelete

    val postsPagingFlow: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 20
        ),
        pagingSourceFactory = { PostsPagingSource(postRepository) }
    ).flow.cachedIn(viewModelScope)

    private val _postLikes = mutableStateMapOf<Long, List<String>>()
    val postLikes: SnapshotStateMap<Long, List<String>> = _postLikes
    private val _topReplies = mutableStateMapOf<Long, Reply?>()

    init {
        loadPosts()
        loadSession()
        loadProfiles()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
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
                // If image doesn't exist or error occurs, return null
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

                // Load comment counts for all posts
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
                // Get current likes from state or post
                val currentLikes = _postLikes[postId]?.toMutableList() ?:
                postRepository.getPostById(postId)?.likes?.toMutableList() ?: mutableListOf()

                // Toggle like
                if (currentLikes.contains(userId)) {
                    currentLikes.remove(userId)
                } else {
                    currentLikes.add(userId)
                }

                // Update local state
                _postLikes[postId] = currentLikes

                // Update backend
                postRepository.updatePostLikes(postId, currentLikes)
            } catch (e: Exception) {
                println("Error toggling like: ${e.message}")
                // Optionally revert the UI state here if the update failed
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
                // Refresh the top reply after liking
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
                _topReplies[postId] = randomReply // We can still use this map for caching
                emit(randomReply)
            } catch (e: Exception) {
                println("Error getting random reply: ${e.message}")
                emit(null)
            }
        }
    }

}