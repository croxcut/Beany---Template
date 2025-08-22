package com.example.data.repositoryImpl.local.db

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.repository.local.datastore.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val context: Context
) : AuthRepository {
    private val Context.dataStore by preferencesDataStore("auth_prefs")
    private val PREFERNCE_KEY = booleanPreferencesKey("is_logged_in")

    override fun isLoggedIn(): Flow<Boolean> =
        context.dataStore.data
            .map { prefs -> prefs[PREFERNCE_KEY] ?: false }

    override suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PREFERNCE_KEY] = loggedIn
        }
    }

}