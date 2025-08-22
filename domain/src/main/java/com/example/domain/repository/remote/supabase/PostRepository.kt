package com.example.domain.repository.remote.supabase

import com.example.domain.model.supabase.NewPost
import com.example.domain.model.supabase.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>

    suspend fun getPosts(page: Int, pageSize: Int): List<Post>
    suspend fun createPost(post: NewPost)
    suspend fun deletePost(postId: Long)
    suspend fun getPostById(postId: Long): Post?
    suspend fun updatePostLikes(postId: Long, likes: List<String>)
    suspend fun getCommentCount(postId: Long): Int

//    suspend fun searchPosts(query: String, page: Int, pageSize: Int): List<Post>
    suspend fun searchPosts(query: String): List<Post>
}