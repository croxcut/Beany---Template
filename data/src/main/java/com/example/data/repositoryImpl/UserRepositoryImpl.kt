package com.example.data.repositoryImpl

import com.example.domain.model.Profile
import com.example.domain.repository.UsersRepository
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