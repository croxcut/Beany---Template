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

package com.example.data.repositoryImpl.local.onboarding

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.repository.local.datastore.OnboardingRepository
import kotlinx.coroutines.flow.Flow
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

//  keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

}