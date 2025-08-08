package com.example.domain.repository

import io.github.jan.supabase.auth.user.UserSession

interface SessionRepository {
    suspend fun getCurrentSession(): Result<UserSession>

    suspend fun updateCurrentSession(): Result<UserSession?>
    suspend fun clearCurrentSession(): Result<Unit>
}