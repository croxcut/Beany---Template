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

import com.example.domain.model.supabase.NewReply
import com.example.domain.model.supabase.Reply
import com.example.domain.repository.remote.supabase.ReplyRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ReplyRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : ReplyRepository {

    @OptIn(SupabaseExperimental::class)
    override suspend fun getRepliesForPost(postId: Long): List<Reply> {
        return supabaseClient.from("replies")
            .selectAsFlow(Reply::id, filter = FilterOperation("post_id", FilterOperator.EQ, postId))
            .first()
    }

    @OptIn(SupabaseExperimental::class)
    override fun getRepliesFlow(postId: Long): Flow<List<Reply>> {
        return supabaseClient.from("replies")
            .selectAsFlow(Reply::id, filter = FilterOperation("post_id", FilterOperator.EQ, postId))
    }

    override suspend fun createReply(reply: NewReply) {
        supabaseClient.from("replies").insert(reply)
    }

    override suspend fun unsendReply(replyId: Long) {
        supabaseClient.from("replies").update(mapOf("reply_body" to "Unsent a message")) {
            filter { eq("id", replyId) }
        }
    }

    @OptIn(SupabaseExperimental::class)
    override suspend fun toggleReplyLike(replyId: Long, userId: String) {
        try {
            val currentReply = supabaseClient.from("replies")
                .selectAsFlow(
                    Reply::id,
                    filter = FilterOperation("id", FilterOperator.EQ, replyId)
                )
                .first()
                .firstOrNull()
                ?: throw Exception("Reply not found")

            val currentLikes = currentReply.likes?.toMutableList() ?: mutableListOf()
            val updatedLikes = if (currentLikes.contains(userId)) {
                currentLikes.apply { remove(userId) }
            } else {
                currentLikes.apply { add(userId) }
            }

            supabaseClient.from("replies")
                .update({
                    set("likes", updatedLikes)
                }) {
                    filter { eq("id", replyId) }
                }
        } catch (e: Exception) {
            println("Error toggling reply like: ${e.message}")
            throw e
        }
    }
}