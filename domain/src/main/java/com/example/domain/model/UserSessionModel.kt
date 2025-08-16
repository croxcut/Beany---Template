package com.example.domain.model

data class UserSessionModel(
    val accessToken: String,
    val tokenType: String,
    val refreshToken: String,
    val expiresIn: Long
)