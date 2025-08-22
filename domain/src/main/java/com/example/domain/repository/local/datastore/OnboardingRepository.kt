package com.example.domain.repository.local.datastore

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository{
    suspend fun setOnboarding(completed: Boolean)
    fun isOnboarded(): Flow<Boolean>
}