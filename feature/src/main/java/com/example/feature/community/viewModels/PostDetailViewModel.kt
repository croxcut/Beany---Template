package com.example.feature.community.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.NewReply
import com.example.domain.model.Post
import com.example.domain.model.Reply
import com.example.domain.repository.PostRepository
import com.example.domain.repository.ReplyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val replyRepository: ReplyRepository
) : ViewModel() {
    private val _post = mutableStateOf<Post?>(null)
    val post: State<Post?> get() = _post

    private val _replies = mutableStateListOf<Reply>()
    val replies: List<Reply> get() = _replies

    private val _parentReplyId = mutableStateOf<Long?>(null)
    val parentReplyId: State<Long?> get() = _parentReplyId

    fun loadPost(postId: Long) {
        viewModelScope.launch {
            try {
                _post.value = postRepository.getPostById(postId)
            } catch (e: Exception) {
                println("Failed to fetch post: ${e.message}")
            }
        }
    }

    fun loadReplies(postId: Long) {
        viewModelScope.launch {
            try {
                _replies.clear()
                _replies.addAll(replyRepository.getRepliesForPost(postId))
            } catch (e: Exception) {
                println("Failed to fetch replies: ${e.message}")
            }
        }
    }

    fun createReply(postId: Long, body: String) {
        if (body.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val reply = NewReply(
                        post_id = postId,
                        sender = "clme@example.com",
                        reply_body = body,
                        parent_reply_id = _parentReplyId.value
                    )
                    replyRepository.createReply(reply)
                    _parentReplyId.value = null
                    loadReplies(postId)
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
                loadReplies(_post.value?.id ?: return@launch)
            } catch (e: Exception) {
                println("Error unsending reply: ${e.message}")
            }
        }
    }
}