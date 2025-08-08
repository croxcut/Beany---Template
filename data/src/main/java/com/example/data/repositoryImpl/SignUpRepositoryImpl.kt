package com.example.data.repositoryImpl

import com.example.domain.model.SignUpCredential
import com.example.domain.repository.SignUpRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
): SignUpRepository {
    override suspend fun signUp(signUpCredential: SignUpCredential): Result<Unit> {
        return try {
            supabaseClient.auth.signUpWith(Email) {
                email = signUpCredential.email
                password = signUpCredential.password
                data = buildJsonObject {
                    put("username", signUpCredential.username)
                    put("fullName", signUpCredential.fullName)
                    put("province", signUpCredential.province)
                    put("farm", signUpCredential.farm)
                    put("registeredAs", signUpCredential.registeredAs)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}