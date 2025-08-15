package com.example.feature.community.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositoryImpl.GetPostFlow
import com.example.data.repositoryImpl.GetRepliesFlowUseCase
import com.example.data.repositoryImpl.SendReplyUseCase
import com.example.domain.model.NewReply
import com.example.domain.model.Post
import com.example.domain.model.Profile
import com.example.domain.model.Reply
import com.example.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostDetailState(
    val post: Post? = null,
    val replies: List<Reply> = emptyList(),
    val parentReplyId: Long? = null, // null = replying to post
    val newReply: String = ""
)

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val getPostFlow: GetPostFlow,
    private val getRepliesFlow: GetRepliesFlowUseCase,
    private val sendReplyUseCase: SendReplyUseCase
) : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    private val _state = MutableStateFlow(PostDetailState())
    val state: StateFlow<PostDetailState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _profile.value = sessionRepository.getUserProfile()
        }
    }

    fun observe(postId: Long) {
        if (postId <= 0) return

        viewModelScope.launch {
            getPostFlow(postId).collect { post ->
                _state.update { it.copy(post = post) }
            }
        }

        viewModelScope.launch {
            getRepliesFlow(postId).collect { replies ->
                _state.update { it.copy(replies = replies) }
            }
        }
    }

    /** Set the parent reply ID (null = replying to post) */
    fun setParentReply(id: Long?) {
        _state.update { it.copy(parentReplyId = id) }
    }

    /** Update the text for the new reply */
    fun updateNewReply(text: String) {
        _state.update { it.copy(newReply = text) }
    }

    fun sendReply(postId: Long) {
        val body = state.value.newReply.trim()
        val profileValue = profile.value
        if (body.isEmpty() || profileValue?.id == null || profileValue.username == null) return

        viewModelScope.launch {
            // If parentReplyId is null, use postId as parent_reply_id
            val parentId = state.value.parentReplyId ?: postId

            sendReplyUseCase(
                NewReply(
                    post_id = postId,
                    user_id = profileValue.id.toString(),
                    sender = profileValue.username.toString(),
                    reply_body = body,
                    parent_reply_id = parentId
                )
            )
            _state.update { it.copy(newReply = "", parentReplyId = null) }
        }
    }

    fun replyToPost(postId: Long?) {
        postId?.let {
            _state.update { it.copy(parentReplyId = postId) }
        }
    }

    /** Call this when replying to a comment */
    fun replyToComment(commentId: Long) {
        setParentReply(commentId)
    }
}
