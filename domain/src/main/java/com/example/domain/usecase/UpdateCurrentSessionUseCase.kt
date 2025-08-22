package com.example.domain.usecase

import com.example.domain.repository.remote.supabase.SessionRepository
import io.github.jan.supabase.auth.user.UserSession
import javax.inject.Inject

class UpdateCurrentSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): Result<UserSession?> {
        return sessionRepository.updateCurrentSession()
    }
}