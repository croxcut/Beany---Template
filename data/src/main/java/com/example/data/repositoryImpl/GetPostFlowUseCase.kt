package com.example.data.repositoryImpl

import com.example.domain.repository.PostRepository
import javax.inject.Inject

class GetPostsFlow @Inject constructor(
    private val repo: PostRepository
) {
    operator fun invoke() = repo.getPostsFlow()
}

class GetPostFlow @Inject constructor(
    private val repo: PostRepository
) {
    operator fun invoke(postId: Long) = repo.getPostFlow(postId)
}