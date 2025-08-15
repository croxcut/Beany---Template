package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Reply(
    val id: Long,
    val post_id: Long,
    val sender: String,
    val reply_body: String,
    val image_url: String? = null, // NEW
    val created_at: String,
    val parent_reply_id: Long? = null
)