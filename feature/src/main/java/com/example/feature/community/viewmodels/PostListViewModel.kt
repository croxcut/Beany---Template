package com.example.feature.community.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositoryImpl.GetPostsFlow
import com.example.domain.model.Post
import com.example.domain.model.Profile
import com.example.domain.repository.PostRepository
import com.example.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val repository: PostRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getPostsFlow().collect { _posts.value = it }
        }

        viewModelScope.launch {
            _profile.value = sessionRepository.getUserProfile()
        }
    }

    fun addPost(body: String) {
        val newPost = Post(
            sender = profile.value?.id.toString(),
            post_body = body
        )
        viewModelScope.launch {
            repository.addPost(newPost)
        }
    }
}
