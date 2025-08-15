package com.example.domain.repository

import com.example.domain.model.NewPost
import com.example.domain.model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun createPost(post: NewPost)
    suspend fun deletePost(postId: Long)
    suspend fun getPostById(postId: Long): Post?
}