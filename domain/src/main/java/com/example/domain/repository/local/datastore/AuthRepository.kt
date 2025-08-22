package com.example.domain.repository.local.datastore

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isLoggedIn(): Flow<Boolean>
    suspend fun setLoggedIn(loggedIn: Boolean)
}