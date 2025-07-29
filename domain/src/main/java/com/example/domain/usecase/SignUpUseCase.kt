package com.example.domain.usecase

import com.example.domain.model.SignUpCredential
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(credential: SignUpCredential): Result<Unit> {
        return authRepository.signUp(credential)
    }
}