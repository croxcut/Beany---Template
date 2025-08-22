package com.example.domain.repository.remote.supabase

import com.example.domain.model.supabase.Profile

interface UpdateProfileRepository {
    suspend fun updateProfile(profile: Profile): Result<Profile>
    suspend fun updateVerified(profileId: String, verified: Boolean): Result<Profile>
}