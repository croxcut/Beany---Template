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

import android.util.Log
import com.example.domain.model.supabase.Profile
import com.example.domain.repository.remote.supabase.UpdateProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class UpdateProfileRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : UpdateProfileRepository {

    override suspend fun updateProfile(profile: Profile): Result<Profile> {
        return try {
            if (profile.id.isNullOrBlank()) {
                return Result.failure(IllegalArgumentException("Profile ID is required to update."))
            }

            supabase.from("profiles").update(
                mapOf(
                    "full_name" to profile.fullName,
                    "username" to profile.username,
                    "province" to profile.province,
                    "farm" to profile.farm,
                    "registered_as" to profile.registeredAs,
                )
            ) {
                filter {
                    eq("id", profile.id!!)
                }
            }

            // 2️⃣ Fetch updated row
            val updatedProfile = supabase.from("profiles").select {
                filter {
                    eq("id", profile.id!!)
                }
            }.decodeSingle<Profile>()

            Log.i("Update-Profile", updatedProfile.username.toString())
            Result.success(updatedProfile)
        } catch (e: Exception) {
            Log.e("Update-Profile", "Error Updating Profile", e)
            Result.failure(e)
        }
    }


    override suspend fun updateVerified(profileId: String, verified: Boolean): Result<Profile> {
        return try {
            if (profileId.isBlank()) {
                return Result.failure(IllegalArgumentException("Profile ID is required"))
            }

            supabase.from("profiles").update(
                mapOf("verified" to verified)
            ) {
                filter { eq("id", profileId) }
            }

            val updatedProfile = supabase.from("profiles").select {
                filter { eq("id", profileId) }
            }.decodeSingle<Profile>()

            Log.i("Update-Verified", "User: $updatedProfile")
            Result.success(updatedProfile)
        } catch (e: Exception) {
            Log.e("Update-Verified", "Error updating verified", e)
            Result.failure(e)
        }
    }
}