package com.example.feature.community.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositoryImpl.GetPostFlow
import com.example.data.repositoryImpl.GetRepliesFlowUseCase
import com.example.data.repositoryImpl.SendReplyUseCase
import com.example.domain.model.NewReply
import com.example.domain.model.Post
import com.example.domain.model.Reply
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
    val parentReplyId: Long? = null,
    val newReply: String = ""
)

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostFlow: GetPostFlow,
    private val getRepliesFlow: GetRepliesFlowUseCase,
    private val sendReplyUseCase: SendReplyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PostDetailState())
    val state: StateFlow<PostDetailState> = _state.asStateFlow()

    fun observe(postId: Long) {
        // idempotent: can be called again safely
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

    fun setParentReply(id: Long?) {
        _state.update { it.copy(parentReplyId = id) }
    }

    fun updateNewReply(text: String) {
        _state.update { it.copy(newReply = text) }
    }

    fun sendReply(postId: Long, sender: String = "clme@example.com") {
        val body = state.value.newReply.trim()
        if (body.isEmpty()) return

        viewModelScope.launch {
            sendReplyUseCase(
                NewReply(
                    post_id = postId,
                    sender = sender,
                    reply_body = body,
                    parent_reply_id = state.value.parentReplyId
                )
            )
            _state.update { it.copy(newReply = "", parentReplyId = null) }
        }
    }
}