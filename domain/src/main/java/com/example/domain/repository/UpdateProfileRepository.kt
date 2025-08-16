package com.example.domain.repository

import com.example.domain.model.Profile

interface UpdateProfileRepository {
    suspend fun updateProfile(profile: Profile): Result<Profile>
}