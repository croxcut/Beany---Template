package com.example.domain.model.supabase

data class UserSession(
    val accessToken: String,
    val tokenType: String,
    val refreshToken: String,
    val expiresIn: Long
)