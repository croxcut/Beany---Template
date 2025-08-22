package com.example.domain.repository.remote.supabase

import com.example.domain.model.supabase.Profile

interface UsersRepository {
    suspend fun getProfiles(): List<Profile>
}