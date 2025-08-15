package com.example.domain.repository

import com.example.domain.model.NewReply
import com.example.domain.model.Reply

interface ReplyRepository {
    suspend fun getRepliesForPost(postId: Long): List<Reply>
    suspend fun createReply(reply: NewReply)
    suspend fun unsendReply(replyId: Long)
}