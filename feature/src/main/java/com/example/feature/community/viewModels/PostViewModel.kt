package com.example.feature.community.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.NewPost
import com.example.domain.model.Post
import com.example.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> get() = _posts

    private val _sortOption = mutableStateOf("Date")
    val sortOption: State<String> = _sortOption

    private val _postToDelete = mutableStateOf<Post?>(null)
    val postToDelete: State<Post?> = _postToDelete

    val sortedPosts: List<Post>
        get() = when (_sortOption.value) {
            "Date" -> _posts.sortedByDescending { it.created_at }
            "Name" -> _posts.sortedBy { it.sender }
            else -> _posts
        }

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            try {
                _posts.clear()
                _posts.addAll(postRepository.getPosts())
            } catch (e: Exception) {
                println("Error loading posts: ${e.message}")
            }
        }
    }

    fun createPost(title: String, body: String) {
        if (title.isNotBlank() && body.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val newPost = NewPost(
                        sender = "clme@example.com",
                        post_title = title,
                        post_body = body
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

    fun setSortOption(option: String) {
        _sortOption.value = option
    }
}