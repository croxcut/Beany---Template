package com.example.domain.usecase

import com.example.domain.model.LoginCredential
import com.example.domain.repository.LoginAuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginAuthRepository: LoginAuthRepository
) {
    suspend operator fun invoke(loginCredential: LoginCredential): Result<Unit> {
        return loginAuthRepository.login(loginCredential)
    }
}