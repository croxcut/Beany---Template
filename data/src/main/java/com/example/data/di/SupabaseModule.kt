package com.example.data.di

import com.example.data.repositoryImpl.LoginAuthRepositoryImpl
import com.example.data.repositoryImpl.PostRepositoryImpl
import com.example.data.repositoryImpl.ReplyRepositoryImpl
import com.example.data.repositoryImpl.ResetPasswordRepositoryImpl
import com.example.data.repositoryImpl.SignUpRepositoryImpl
import com.example.data.repositoryImpl.UpdateProfileRepositoryImpl
import com.example.data.repositoryImpl.UserRepositoryImpl
import com.example.domain.repository.LoginAuthRepository
import com.example.domain.repository.PostRepository
import com.example.domain.repository.ReplyRepository
import com.example.domain.repository.ResetPasswordRepository
import com.example.domain.repository.SignUpRepository
import com.example.domain.repository.UpdateProfileRepository
import com.example.domain.repository.UsersRepository
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


    @Provides
    @Singleton
    fun provideReplyRepository(supabaseClient: SupabaseClient): ReplyRepository {
        return ReplyRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun providePostRepository(supabaseClient: SupabaseClient): PostRepository {
        return PostRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideUserRepository(supabaseClient: SupabaseClient): UsersRepository {
        return UserRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideResetPasswordRepository(supabaseClient: SupabaseClient): ResetPasswordRepository {
        return ResetPasswordRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideUpdateProfileRepository(supabaseClient: SupabaseClient): UpdateProfileRepository {
        return UpdateProfileRepositoryImpl(supabaseClient)
    }

}