package com.example.data.repositoryImpl.remote.supabase.auth

import android.util.Log
import com.example.domain.model.supabase.LoginCredential
import com.example.domain.model.supabase.Profile
import com.example.domain.repository.remote.supabase.LoginAuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class LoginAuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
): LoginAuthRepository {
    override suspend fun login(loginCredential: LoginCredential): Result<Unit> = runCatching {
        supabaseClient.auth.signInWith(Email) {
            email = loginCredential.email
            password = loginCredential.password
        }
        val session: UserSession? = supabaseClient.auth.currentSessionOrNull()
        val userId = session?.user?.id ?: throw Exception("Unable To Fetch Data")

        val profile = supabaseClient.from("profiles")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingle<Profile>()

        Log.i("Session", "Name: ${profile.fullName}")
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> = runCatching {
        supabaseClient.auth.resetPasswordForEmail(
            email = email,
            redirectUrl = "beanyapp://password-reset"
        )
    }

    override suspend fun exchangeRecoveryToken(accessToken: String): Result<Unit> = runCatching {
        supabaseClient.auth.exchangeCodeForSession(
            code = accessToken
        )
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> = runCatching {
        supabaseClient.auth.updateUser {
            password = newPassword
        }
    }

    // java -jar bundletool-all-1.18.1.jar build-apks --bundle=app-release.aab --output=app.apks --mode=universal
}