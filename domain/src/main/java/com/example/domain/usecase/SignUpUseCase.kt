package com.example.domain.usecase

import com.example.domain.model.UserCredential
import com.example.domain.repository.SignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
) {
    suspend operator fun invoke(credential: UserCredential): Result<Unit>{
        return signUpRepository.signUp(credential)
    }
}