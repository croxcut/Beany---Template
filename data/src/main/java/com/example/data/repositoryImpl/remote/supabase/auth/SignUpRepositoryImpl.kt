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

import com.example.domain.model.supabase.SignUpCredential
import com.example.domain.repository.remote.supabase.SignUpRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : SignUpRepository {
    override suspend fun signUp(signUpCredential: SignUpCredential): Result<String> {
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
                    put("verified", signUpCredential.verified)
                }
            }

            val currentUser = supabaseClient.auth.currentUserOrNull()
                ?: throw IllegalStateException("User not found after signup")

            Result.success(currentUser.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}