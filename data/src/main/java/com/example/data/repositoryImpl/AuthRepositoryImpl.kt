package com.example.data.repositoryImpl


import com.example.domain.model.LoginCredential
import com.example.domain.model.SignUpCredential
import com.example.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AuthRepository {
    override suspend fun login(credential: LoginCredential): Result<Unit> {
        return try {
            supabaseClient.auth.signInWith(Email) {
                email = credential.email
                password = credential.password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}