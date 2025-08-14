package com.example.domain.model

data class PostWithReplies(
    val post: Post,
    val replies: List<Reply>
)