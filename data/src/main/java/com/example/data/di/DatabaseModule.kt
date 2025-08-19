package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.dao.NotificationDao
import com.example.data.local.db.DiagnosisDatabase
import com.example.data.local.db.NotificationDatabase
import com.example.data.local.repository.DiagnosisRepository
import com.example.data.local.repository.NotificationRepository
import com.example.data.repositoryImpl.DiagnosisRepositoryImpl
import com.example.data.repositoryImpl.local.NotificationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDiagnosisDatabase(@ApplicationContext context: Context): DiagnosisDatabase =
        Room.databaseBuilder(
            context = context,
            klass = DiagnosisDatabase::class.java,
            name = "diagnosis_db"
        ).build()

    @Provides
    @Singleton
    fun provideDiagnosisRepository(db: DiagnosisDatabase): DiagnosisRepository {
        return DiagnosisRepositoryImpl(db.diagnosisDao())
    }

    @Provides
    @Singleton
    fun provideNotificationsDatabase(context: Context): NotificationDatabase {
        return Room.databaseBuilder(
            context,
            NotificationDatabase::class.java,
            "notification_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(db: NotificationDatabase): NotificationRepository {
        return NotificationRepositoryImpl(db.notificationDao())
    }


}