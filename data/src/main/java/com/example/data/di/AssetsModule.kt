package com.example.data.di

import android.content.Context
import com.example.data.repositoryImpl.local.TermsRepositoryImpl
import com.example.domain.repository.local.TermsRepository
import com.example.domain.usecase.GetTermsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AssetsModule {


    @Provides
    @Singleton
    fun provideTermsRepository(@ApplicationContext context: Context): TermsRepository {
        return TermsRepositoryImpl(context.assets)
    }

    @Provides
    @Singleton
    fun provideGetTermsUseCase(repo: TermsRepository): GetTermsUseCase {
        return GetTermsUseCase(repo)
    }

}