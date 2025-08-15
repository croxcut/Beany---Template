package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NewReply(
    val post_id: Long,
    val user_id: String, // UUID of logged-in user
    val sender: String,
    val reply_body: String,
    val parent_reply_id: Long? = null
)