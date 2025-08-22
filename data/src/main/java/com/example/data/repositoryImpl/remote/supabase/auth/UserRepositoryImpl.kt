package com.example.data.repositoryImpl.remote.supabase.auth

import com.example.domain.model.supabase.Profile
import com.example.domain.repository.remote.supabase.UsersRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
): UsersRepository {
    override suspend fun getProfiles(): List<Profile> {
        return supabaseClient
                .from("profiles")
                .select()
                .decodeList<Profile>()
    }
}