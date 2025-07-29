package com.example.domain.usecase

import com.example.domain.model.LoginCredential
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(credential: LoginCredential): Result<Unit> {
        return repository.login(credential)
    }
}