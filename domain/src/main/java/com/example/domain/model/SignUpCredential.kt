package com.example.domain.model

data class SignUpCredential(
    val username: String,
    val password: String,
    val fullName: String,
    val province: String,
    val farm: String
)
