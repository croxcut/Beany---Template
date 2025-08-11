package com.example.data.di

import com.example.data.repositoryImpl.SessionRepositoryImpl
import com.example.domain.repository.SessionRepository
import com.example.domain.usecase.ClearCurrentSessionUseCase
import com.example.domain.usecase.GetCurrentSessionUseCase
import com.example.domain.usecase.UpdateCurrentSessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {


    @Provides
    @Singleton
    fun provideSessionRepository(supabaseClient: SupabaseClient): SessionRepository {
        return SessionRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun providesCurrentSessionUseCase(sessionRepository: SessionRepository): GetCurrentSessionUseCase{
        return GetCurrentSessionUseCase(sessionRepository)
    }

    @Provides
    @Singleton
    fun providesUpdateCurrentSession(sessionRepository: SessionRepository): UpdateCurrentSessionUseCase {
        return UpdateCurrentSessionUseCase(sessionRepository)
    }

    @Provides
    @Singleton
    fun providesClearCurrentSessionUseCase(sessionRepository: SessionRepository): ClearCurrentSessionUseCase {
        return ClearCurrentSessionUseCase(sessionRepository)
    }

}