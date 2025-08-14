package com.example.data.di

import com.example.data.repositoryImpl.PostRepositoryImpl
import com.example.domain.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePostRepository(supabaseClient: SupabaseClient): PostRepository {
        return PostRepositoryImpl(supabaseClient)
    }
}