package com.example.data.di

import android.content.Context
import com.example.data.repositoryImpl.OnboardingRepositoryImpl
import com.example.domain.repository.OnboardingRepository
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