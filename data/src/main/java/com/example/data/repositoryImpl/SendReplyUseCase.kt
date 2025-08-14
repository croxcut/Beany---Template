package com.example.data.repositoryImpl

import com.example.domain.model.NewReply
import com.example.domain.repository.PostRepository
import javax.inject.Inject


class SendReplyUseCase @Inject constructor(
    private val repo: PostRepository
) {
    suspend operator fun invoke(reply: NewReply) = repo.sendReply(reply)
}