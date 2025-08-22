package com.example.domain.repository.remote.supabase

import com.example.domain.model.supabase.Profile
import io.github.jan.supabase.auth.user.UserSession

interface SessionRepository {
    suspend fun getCurrentSession(): UserSession?
    suspend fun getUserProfile(): Profile?
    suspend fun updateCurrentSession(): Result<UserSession?>
    suspend fun clearCurrentSession(): Result<Unit>
    suspend fun isSignedUp(): Boolean
}