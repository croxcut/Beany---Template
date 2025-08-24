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
import com.example.data.repositoryImpl.local.ml.DetectionRepositoryImpl
import com.example.data.tflite.ModelRunner
import com.example.domain.repository.local.ml.DetectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TFLiteModule {

    @Provides
    @Singleton
    fun provideModelRunner(@ApplicationContext context: Context): ModelRunner {
        return ModelRunner(
            context = context,
            modelPath = "model.tflite",
            labelPath = "labels.txt"
        )
    }

    @Provides
    @Singleton
    fun provideDetectionRepository(modelRunner: ModelRunner): DetectionRepository {
        return DetectionRepositoryImpl(modelRunner)
    }

}