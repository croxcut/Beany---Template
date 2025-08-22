package com.example.domain.model.supabase

import kotlinx.serialization.Serializable

@Serializable
data class NewPost(
    val sender: String,
    val post_title: String,
    val post_body: String,
    val image_url: String? = null,
    val tags: List<String>? = null
)