package com.example.data.repositoryImpl

import com.example.domain.model.NewPost
import com.example.domain.model.Post
import com.example.domain.repository.PostRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
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
}