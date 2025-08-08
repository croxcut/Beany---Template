package com.example.data.repositoryImpl

import android.util.Log
import com.example.domain.model.LoginCredential
import com.example.domain.model.Profile
import com.example.domain.repository.LoginAuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class LoginAuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
): LoginAuthRepository{
    override suspend fun login(loginCredential: LoginCredential): Result<Unit> {
        return try {
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
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}