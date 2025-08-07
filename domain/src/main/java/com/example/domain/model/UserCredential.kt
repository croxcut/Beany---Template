package com.example.domain.model

data class UserCredential(
    val username: String,
    val fullName: String,
    val email: String,
    val password: String,
    val province: String,
    val farm: String,
    val registeredAs: String
)