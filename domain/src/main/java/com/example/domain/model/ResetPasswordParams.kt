package com.example.domain.model

data class ResetPasswordParams(
    val token: String,
    val newPassword: String
)
