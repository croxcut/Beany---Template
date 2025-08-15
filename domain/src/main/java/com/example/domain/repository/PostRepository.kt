package com.example.domain.repository

import com.example.domain.model.NewReply
import com.example.domain.model.Post
import com.example.domain.model.Reply
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPostsFlow(): Flow<List<Post>>
    fun getPostFlow(postId: Long): Flow<Post?>
    fun getRepliesFlow(postId: Long): Flow<List<Reply>>
    suspend fun sendReply(reply: NewReply)
    suspend fun addPost(post: Post)
}