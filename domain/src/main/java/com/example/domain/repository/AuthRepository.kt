package com.example.domain.repository

import com.example.domain.model.LoginCredential
import com.example.domain.model.SignUpCredential


interface AuthRepository{
    suspend fun login(credential: LoginCredential): Result<Unit>
}