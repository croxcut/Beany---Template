package com.example.data.repositoryImpl

import com.example.domain.model.NewReply
import com.example.domain.model.Reply
import com.example.domain.repository.ReplyRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
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

    override suspend fun createReply(reply: NewReply) {
        supabaseClient.from("replies").insert(reply)
    }

    override suspend fun unsendReply(replyId: Long) {
        supabaseClient.from("replies").update(mapOf("reply_body" to "Unsent a message")) {
            filter { eq("id", replyId) }
        }
    }
}