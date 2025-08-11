package com.example.data.di

import com.example.data.repositoryImpl.BucketRepositoryImpl
import com.example.domain.repository.BucketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BucketModule {

    @Provides
    @Singleton
    fun provideBucketRepository(supabaseClient: SupabaseClient): BucketRepository {
        return BucketRepositoryImpl(supabaseClient)
    }

}
