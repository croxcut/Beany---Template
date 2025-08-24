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
import androidx.room.Room
import com.example.data.local.dao.ActivityDao
import com.example.data.local.db.ActivityDatabase
import com.example.data.local.db.DiagnosisDatabase
import com.example.data.local.db.NotificationDatabase
import com.example.data.local.repository.DiagnosisRepository
import com.example.data.local.repository.NotificationRepository
import com.example.data.repositoryImpl.local.ml.DiagnosisRepositoryImpl
import com.example.data.repositoryImpl.local.notif.NotificationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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


    @Provides
    @Singleton
    fun provideActivityDatabase(@ApplicationContext app: Context): ActivityDatabase =
        Room.databaseBuilder(app, ActivityDatabase::class.java, "app_db")
            .build()

    @Provides
    fun provideActivityDao(db: ActivityDatabase): ActivityDao = db.activityDao()

}