// ===============================================================================
//
// Copyright (C) 2025-2026 by John Paul Valenzuela
//
// This source is available for distribution and/or modification
// only under the terms of the Beany Source Code License as
// published by Beany. All rights reserved.
//
// The source is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// FITNESS FOR A PARTICULAR PURPOSE. See the Beany Source Code License
// for more details.
//
// ===============================================================================

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