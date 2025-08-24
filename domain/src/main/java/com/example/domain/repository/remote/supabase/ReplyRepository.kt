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

import com.example.domain.model.supabase.NewReply
import com.example.domain.model.supabase.Reply
import kotlinx.coroutines.flow.Flow

interface ReplyRepository {
    suspend fun getRepliesForPost(postId: Long): List<Reply>
    fun getRepliesFlow(postId: Long): Flow<List<Reply>>
    suspend fun createReply(reply: NewReply)
    suspend fun unsendReply(replyId: Long)
    suspend fun toggleReplyLike(replyId: Long, userId: String)
}