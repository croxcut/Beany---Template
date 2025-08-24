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

package com.example.data.di

import android.content.Context
import com.example.data.repositoryImpl.local.onboarding.OnboardingRepositoryImpl
import com.example.domain.repository.local.datastore.OnboardingRepository
import com.example.domain.usecase.IsOnboardedUseCase
import com.example.domain.usecase.SetOnboardingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule{

    @Provides
    @Singleton
    fun provideOnboardingRepository(
        @ApplicationContext context: Context
    ): OnboardingRepository = OnboardingRepositoryImpl(context)

    @Provides
    fun provideSetOnboardingUseCase(
        repo: OnboardingRepository
    ) = SetOnboardingUseCase(repo)

    @Provides
    fun provideIsOnboarded(
        repo: OnboardingRepository
    ) = IsOnboardedUseCase(repo)

}