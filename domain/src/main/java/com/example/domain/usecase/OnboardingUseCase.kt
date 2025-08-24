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