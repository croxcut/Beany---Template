package com.example.domain.repository

import com.example.domain.model.NewReply
import com.example.domain.model.Reply
import kotlinx.coroutines.flow.Flow

interface ReplyRepository {
    suspend fun getRepliesForPost(postId: Long): List<Reply>
    fun getRepliesFlow(postId: Long): Flow<List<Reply>>
    suspend fun createReply(reply: NewReply)
    suspend fun unsendReply(replyId: Long)
    suspend fun toggleReplyLike(replyId: Long, userId: String)
}