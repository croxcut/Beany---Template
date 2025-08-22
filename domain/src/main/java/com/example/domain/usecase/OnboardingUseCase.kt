package com.example.domain.usecase

import com.example.domain.repository.local.datastore.OnboardingRepository
import kotlinx.coroutines.flow.Flow

class SetOnboardingUseCase(
    private val repo: OnboardingRepository
) {
    suspend operator fun invoke(completed: Boolean) = repo.setOnboarding(completed)
}

class IsOnboardedUseCase(
    private val repo: OnboardingRepository
) {
    operator fun invoke(): Flow<Boolean> = repo.isOnboarded()
}