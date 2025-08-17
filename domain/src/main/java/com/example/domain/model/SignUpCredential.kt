package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpCredential(
    val username: String,
    val fullName: String,
    val email: String,
    val password: String,
    val province: String,
    val farm: String,
    val registeredAs: String,
    val verified: Boolean = false
)