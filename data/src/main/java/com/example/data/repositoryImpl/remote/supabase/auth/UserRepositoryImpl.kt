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