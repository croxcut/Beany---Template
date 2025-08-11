package com.example.domain.usecase

import com.example.domain.repository.SessionRepository
import javax.inject.Inject

class ClearCurrentSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return sessionRepository.clearCurrentSession()
    }
}