package com.example.data.repositoryImpl

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.example.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class OnboardingRepositoryImpl(
    private val context: Context
): OnboardingRepository {

    private val Context.dataStore by preferencesDataStore("onboarding_prefs")
    private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_complete")

    override suspend fun setOnboarding(completed: Boolean) {
        context.dataStore.edit { prefs -> prefs[ONBOARDING_KEY] = completed }
    }

    override fun isOnboarded(): Flow<Boolean> {
        return context.dataStore.data.map { it[ONBOARDING_KEY] ?: false }
    }

}

//  keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android