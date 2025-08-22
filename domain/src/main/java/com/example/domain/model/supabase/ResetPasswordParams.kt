package com.example.domain.model.supabase

data class ResetPasswordParams(
    val token: String,
    val newPassword: String
)