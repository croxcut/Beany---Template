package com.example.data.di

import com.example.data.repositoryImpl.GoogleAuthRepositoryImpl
import com.example.domain.repository.GoogleAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthModule {

    @Provides
    @Singleton
    fun provideGoogleAuthRepository(
        supabaseClient: SupabaseClient
    ): GoogleAuthRepository = GoogleAuthRepositoryImpl(supabaseClient)

}