package com.example.data.repositoryImpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.domain.model.Post
import com.example.domain.repository.PostRepository

class PostsPagingSource(
    private val postRepository: PostRepository
) : PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            // ⚠️ Implement getPosts(page, pageSize) in your PostRepository
            val posts = postRepository.getPosts(page, pageSize)

            LoadResult.Page(
                data = posts,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (posts.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}