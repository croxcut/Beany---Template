package com.example.domain.repository

import com.example.domain.model.Profile

interface UsersRepository {
    suspend fun getProfiles(): List<Profile>
}