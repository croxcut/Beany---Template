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

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.domain.model.supabase.Post
import com.example.domain.repository.remote.supabase.PostRepository

class PostsPagingSource(
    private val postRepository: PostRepository,
    private val searchQuery: String? = null,
    private val selectedTags: List<String> = emptyList()
) : PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val page = params.key ?: 1

            val posts = when {
                !searchQuery.isNullOrBlank() -> {
                    postRepository.searchPosts(searchQuery)
                        .filter { post ->
                            selectedTags.isEmpty() ||
                                    post.tags?.any { it in selectedTags } ?: false
                        }
                }
                else -> {
                    postRepository.getPosts(page, params.loadSize)
                        .filter { post ->
                            selectedTags.isEmpty() ||
                                    post.tags?.any { it in selectedTags } ?: false
                        }
                }
            }

            LoadResult.Page(
                data = posts,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (posts.size < params.loadSize) null else page + 1
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