package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long? = null,  // make nullable so Supabase can auto-generate
    val sender: String,
    val post_body: String? = null,
    val created_at: String? = null
)
