package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val sender: String,
    val post_title: String,
    val post_body: String? = null,
    val image_url: String? = null,
    val created_at: String
)
