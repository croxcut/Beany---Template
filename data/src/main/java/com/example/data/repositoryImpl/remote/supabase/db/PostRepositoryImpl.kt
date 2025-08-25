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

package com.example.data.repositoryImpl.remote.supabase.db

import com.example.domain.model.supabase.NewPost
import com.example.domain.model.supabase.Post
import com.example.domain.model.supabase.Reply
import com.example.domain.repository.remote.supabase.PostRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : PostRepository {

    @OptIn(SupabaseExperimental::class)
    override suspend fun getPosts(): List<Post> {
        return supabaseClient.from("posts").selectAsFlow(Post::id).first()
    }

    @OptIn(SupabaseExperimental::class)
    override suspend fun getPosts(page: Int, pageSize: Int): List<Post> {
        val start = (page - 1) * pageSize
        val end = start + pageSize - 1

        return supabaseClient.from("posts")
            .select {
                order(column = "created_at", order = Order.DESCENDING)
                range(start.toLong()..end.toLong())
            }
            .decodeList<Post>()
    }

    @OptIn(SupabaseExperimental::class)
    override suspend fun createPost(post: NewPost) {
        supabaseClient.from("posts").insert(post)
    }

    override suspend fun deletePost(postId: Long) {
        supabaseClient.from("replies").delete { filter { eq("post_id", postId) } }
        supabaseClient.from("posts").delete { filter { eq("id", postId) } }
    }

    @OptIn(SupabaseExperimental::class)
    override suspend fun getPostById(postId: Long): Post? {
        return supabaseClient.from("posts")
            .selectAsFlow(Post::id, filter = FilterOperation("id", FilterOperator.EQ, postId))
            .first()
            .firstOrNull()
    }

    override suspend fun updatePostLikes(postId: Long, likes: List<String>) {
        supabaseClient.from("posts")
            .update({
                set("likes", likes)
            }) {
                filter { eq("id", postId) }
            }
    }

    @OptIn(SupabaseExperimental::class)
    override suspend fun getCommentCount(postId: Long): Int {
        return try {
            supabaseClient.from("replies")
                .selectAsFlow(
                    Reply::id,
                    filter = FilterOperation("post_id", FilterOperator.EQ, postId)
                )
                .first()
                .count()
        } catch (e: Exception) {
            println("Error getting reply count: ${e.message}")
            0
        }
    }

//    @OptIn(SupabaseExperimental::class)
//    override suspend fun searchPosts(query: String, page: Int, pageSize: Int): List<Post> {
//        val start = (page - 1) * pageSize
//        val end = start + pageSize - 1
//
//        return supabaseClient.from("posts")
//            .selectAsFlow(
//                Post::id,
//                filter = FilterOperation("post_title", FilterOperator.ILIKE, "%$query%")
//            )
//            .first()
//            .let { posts ->
//                posts.sortedByDescending { it.created_at }
//                    .drop(start)
//                    .take(pageSize)
//            }
//    }

    @OptIn(SupabaseExperimental::class)
    override suspend fun searchPosts(query: String): List<Post> {
        return supabaseClient.from("posts")
            .select {
                filter {
                    ilike("post_title", "%$query%")
                }
            }
            .decodeList<Post>()
    }

}