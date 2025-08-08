package com.example.domain.repository

import com.example.domain.model.LoginCredential

interface LoginAuthRepository {
    suspend fun login(loginCredential: LoginCredential) : Result<Unit>
}