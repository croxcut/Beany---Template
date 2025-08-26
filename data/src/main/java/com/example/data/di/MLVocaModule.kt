package com.example.data.di

import com.example.data.remote.services.MLVocaApiService
import com.example.data.repositoryImpl.remote.mlvoca.MLVocaRepositoryImpl
import com.example.domain.repository.remote.mlvoca.MLVocaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MLVocaModule {

    @Provides
    @Singleton
    @Named("MLVocaOkHttpClient")
    fun provideMLVocaOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideMLVocaApiService(@Named("MLVocaOkHttpClient") okHttpClient: OkHttpClient): MLVocaApiService {
        return MLVocaApiService(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideMLVocaRepository(apiService: MLVocaApiService): MLVocaRepository {
        return MLVocaRepositoryImpl(apiService)
    }
}