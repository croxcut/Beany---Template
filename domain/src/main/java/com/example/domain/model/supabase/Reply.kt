package com.example.domain.model.supabase

import kotlinx.serialization.Serializable

@Serializable
data class Reply(
    val id: Long,
    val post_id: Long,
    val sender: String,
    val reply_body: String,
    val image_url: String? = null,
    val created_at: String,
    val parent_reply_id: Long? = null,
    val likes: List<String>? = emptyList() // Add this line
)