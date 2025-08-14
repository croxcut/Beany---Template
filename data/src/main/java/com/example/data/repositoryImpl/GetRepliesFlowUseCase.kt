package com.example.data.repositoryImpl

import com.example.domain.repository.PostRepository
import javax.inject.Inject

class GetRepliesFlowUseCase @Inject constructor(
    private val repo: PostRepository
) {
    operator fun invoke(postId: Long) = repo.getRepliesFlow(postId)
}