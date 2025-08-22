package com.example.data.repositoryImpl.remote.supabase.auth

import com.example.domain.repository.remote.supabase.ResetPasswordRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.ExperimentalTime

@Singleton
class ResetPasswordRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : ResetPasswordRepository {

    override suspend fun sendResetEmail(email: String, redirectUrl: String) {
        supabase.auth.resetPasswordForEmail(email = email, redirectUrl = redirectUrl)
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun importSession(session: UserSession) {
        val userSession = UserSession(
            accessToken = session.accessToken,
            tokenType = session.tokenType,
            refreshToken = session.refreshToken,
            expiresIn = session.expiresIn,
            user = null
        )
        supabase.auth.importSession(userSession)
    }

    override suspend fun updatePassword(newPassword: String) {
        supabase.auth.updateUser { password = newPassword }
    }
}