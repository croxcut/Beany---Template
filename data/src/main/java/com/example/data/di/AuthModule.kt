package com.example.data.di

import com.example.data.repositoryImpl.AuthRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.usecase.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://jecwhxyzkqyebgcopsvo.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImplY3doeHl6a3F5ZWJnY29wc3ZvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM0MTQ3NzksImV4cCI6MjA2ODk5MDc3OX0.WI7B5RFiKO4O2ZgR2vVkmj2wzm5MPsS1jWrTag0tI6w"
        ) {
            install(Auth)
            install(Postgrest)
        }
    }

    @Provides
    @Singleton
    fun provideAuthRepository(supabaseClient: SupabaseClient): AuthRepository {
        return AuthRepositoryImpl(supabaseClient)
    }

    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

}
