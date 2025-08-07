package com.example.data.repositoryImpl

import com.example.domain.model.UserCredential
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
    override suspend fun signUp(userCredential: UserCredential): Result<Unit> {
        return try {
            supabaseClient.auth.signUpWith(Email) {
                email = userCredential.email
                password = userCredential.password
                data = buildJsonObject {
                    put("username", userCredential.username)
                    put("fullName", userCredential.fullName)
                    put("province", userCredential.province)
                    put("farm", userCredential.farm)
                    put("registeredAs", userCredential.registeredAs)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}