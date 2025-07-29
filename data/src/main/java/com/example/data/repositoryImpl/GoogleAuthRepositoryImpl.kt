package com.example.data.repositoryImpl

import com.example.domain.repository.GoogleAuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import javax.inject.Inject

class GoogleAuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : GoogleAuthRepository {

    override suspend fun signInWithGoogle(idToken: String): Boolean {
        return try {
            supabaseClient.auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
                nonce = null
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}