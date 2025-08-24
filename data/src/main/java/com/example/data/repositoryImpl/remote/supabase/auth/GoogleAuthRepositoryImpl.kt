// ===============================================================================
//
// Copyright (C) 2025-2026 by John Paul Valenzuela
//
// This source is available for distribution and/or modification
// only under the terms of the Beany Source Code License as
// published by Beany. All rights reserved.
//
// The source is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// FITNESS FOR A PARTICULAR PURPOSE. See the Beany Source Code License
// for more details.
//
// ===============================================================================

package com.example.data.repositoryImpl.remote.supabase.auth

import com.example.domain.repository.remote.supabase.GoogleAuthRepository
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