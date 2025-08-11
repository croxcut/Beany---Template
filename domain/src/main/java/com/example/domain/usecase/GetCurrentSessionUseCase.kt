package com.example.domain.usecase

import com.example.domain.repository.SessionRepository
import io.github.jan.supabase.auth.user.UserSession
import javax.inject.Inject

class GetCurrentSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke() : UserSession? {
        return sessionRepository.getCurrentSession()
    }
}