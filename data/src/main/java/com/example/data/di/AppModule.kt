package com.example.data.di

import android.content.Context
import com.example.data.repositoryImpl.local.notif.NotificationHandleRepositoryImpl
import com.example.domain.repository.local.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotificationRepository(@ApplicationContext context: Context): NotificationRepository {
        return NotificationHandleRepositoryImpl(context)
    }

}