package com.example.domain.repository.remote.supabase

import io.github.jan.supabase.auth.user.UserSession

interface ResetPasswordRepository {
    suspend fun sendResetEmail(email: String, redirectUrl: String)
    suspend fun importSession(session: UserSession)
    suspend fun updatePassword(newPassword: String)
}
