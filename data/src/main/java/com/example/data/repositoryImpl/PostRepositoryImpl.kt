package com.example.data.repositoryImpl

import com.example.domain.model.NewReply
import com.example.domain.model.Post
import com.example.domain.model.Reply
import com.example.domain.repository.PostRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val client: SupabaseClient
) : PostRepository {

    @OptIn(SupabaseExperimental::class)
    override fun getPostsFlow(): Flow<List<Post>> =
        client.from("posts").selectAsFlow(Post::id)

    @OptIn(SupabaseExperimental::class)
    override fun getPostFlow(postId: Long): Flow<Post?> =
        client.from("posts")
            .selectAsFlow(Post::id, filter = FilterOperation("id", FilterOperator.EQ, postId))
            .map { it.firstOrNull() }

    @OptIn(SupabaseExperimental::class)
    override fun getRepliesFlow(postId: Long): Flow<List<Reply>> =
        client.from("replies")
            .selectAsFlow(Reply::id, filter = FilterOperation("post_id", FilterOperator.EQ, postId))

    override suspend fun sendReply(reply: NewReply) {
        client.from("replies").insert(reply)
    }

}