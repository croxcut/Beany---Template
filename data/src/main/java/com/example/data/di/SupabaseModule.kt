package com.example.data.di

import com.example.data.repositoryImpl.LoginAuthRepositoryImpl
import com.example.data.repositoryImpl.SignUpRepositoryImpl
import com.example.domain.repository.LoginAuthRepository
import com.example.domain.repository.SignUpRepository
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://moaafjxlduuwjpbgrheo.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1vYWFmanhsZHV1d2pwYmdyaGVvIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1NDM0NzMwMywiZXhwIjoyMDY5OTIzMzAzfQ.4QFmiQQMMsVmPJrt9o4R951OfNXRK-HEXjJkIA1r9JA",

        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideSignUpRepository(supabaseClient: SupabaseClient): SignUpRepository {
        return SignUpRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(signUpRepository: SignUpRepository): SignUpUseCase {
        return SignUpUseCase(signUpRepository)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(supabaseClient: SupabaseClient): LoginAuthRepository {
        return LoginAuthRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(loginAuthRepository: LoginAuthRepository): LoginUseCase {
        return LoginUseCase(loginAuthRepository)
    }


}