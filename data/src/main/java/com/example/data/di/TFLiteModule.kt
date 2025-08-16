package com.example.data.di

import android.content.Context
import com.example.data.repositoryImpl.DetectionRepositoryImpl
import com.example.data.tflite.ModelRunner
import com.example.domain.repository.DetectionRepository
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