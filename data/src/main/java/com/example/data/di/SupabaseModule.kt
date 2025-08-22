package com.example.data.di

import com.example.data.repositoryImpl.remote.supabase.storage.BucketRepositoryImpl
import com.example.data.repositoryImpl.remote.supabase.auth.GoogleAuthRepositoryImpl
import com.example.data.repositoryImpl.remote.supabase.auth.LoginAuthRepositoryImpl
import com.example.data.repositoryImpl.remote.supabase.db.PostRepositoryImpl
import com.example.data.repositoryImpl.remote.supabase.db.ReplyRepositoryImpl
import com.example.data.repositoryImpl.remote.supabase.auth.ResetPasswordRepositoryImpl
import com.example.data.repositoryImpl.remote.supabase.auth.SessionRepositoryImpl
import com.example.data.repositoryImpl.remote.supabase.auth.SignUpRepositoryImpl
import com.example.data.repositoryImpl.remote.supabase.auth.UpdateProfileRepositoryImpl
import com.example.data.repositoryImpl.remote.supabase.auth.UserRepositoryImpl
import com.example.domain.repository.remote.supabase.BucketRepository
import com.example.domain.repository.remote.supabase.GoogleAuthRepository
import com.example.domain.repository.remote.supabase.LoginAuthRepository
import com.example.domain.repository.remote.supabase.PostRepository
import com.example.domain.repository.remote.supabase.ReplyRepository
import com.example.domain.repository.remote.supabase.ResetPasswordRepository
import com.example.domain.repository.remote.supabase.SessionRepository
import com.example.domain.repository.remote.supabase.SignUpRepository
import com.example.domain.repository.remote.supabase.UpdateProfileRepository
import com.example.domain.repository.remote.supabase.UsersRepository
import com.example.domain.usecase.ClearCurrentSessionUseCase
import com.example.domain.usecase.GetCurrentSessionUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.SignUpUseCase
import com.example.domain.usecase.UpdateCurrentSessionUseCase
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

    @Provides
    @Singleton
    fun provideBucketRepository(supabaseClient: SupabaseClient): BucketRepository {
        return BucketRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthRepository(
        supabaseClient: SupabaseClient
    ): GoogleAuthRepository = GoogleAuthRepositoryImpl(supabaseClient)

    @Provides
    @Singleton
    fun provideSessionRepository(supabaseClient: SupabaseClient): SessionRepository {
        return SessionRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun providesCurrentSessionUseCase(sessionRepository: SessionRepository): GetCurrentSessionUseCase{
        return GetCurrentSessionUseCase(sessionRepository)
    }

    @Provides
    @Singleton
    fun providesUpdateCurrentSession(sessionRepository: SessionRepository): UpdateCurrentSessionUseCase {
        return UpdateCurrentSessionUseCase(sessionRepository)
    }

    @Provides
    @Singleton
    fun providesClearCurrentSessionUseCase(sessionRepository: SessionRepository): ClearCurrentSessionUseCase {
        return ClearCurrentSessionUseCase(sessionRepository)
    }

}