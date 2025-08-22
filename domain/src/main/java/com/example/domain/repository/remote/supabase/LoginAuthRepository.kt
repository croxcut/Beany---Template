package com.example.domain.repository.remote.supabase

import com.example.domain.model.supabase.LoginCredential

interface LoginAuthRepository {
    suspend fun login(loginCredential: LoginCredential) : Result<Unit>
    suspend fun sendPasswordResetEmail(email: String) : Result<Unit>
    suspend fun exchangeRecoveryToken(accessToken: String) : Result<Unit>
    suspend fun updatePassword(newPassword: String) : Result<Unit>
}