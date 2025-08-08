package com.example.domain.usecase

import com.example.domain.model.SignUpCredential
import com.example.domain.repository.SignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
) {
    suspend operator fun invoke(credential: SignUpCredential): Result<Unit>{
        return signUpRepository.signUp(credential)
    }
}