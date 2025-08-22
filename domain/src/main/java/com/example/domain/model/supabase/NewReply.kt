package com.example.domain.model.supabase

import kotlinx.serialization.Serializable

@Serializable
data class NewReply(
    val post_id: Long,
    val sender: String,
    val reply_body: String,
    val image_url: String? = null, // NEW
    val parent_reply_id: Long? = null
)