package com.example.domain.usecase

import com.example.domain.model.supabase.SignUpCredential
import com.example.domain.repository.remote.supabase.SignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
) {
    suspend operator fun invoke(credential: SignUpCredential): Result<String>{
        return signUpRepository.signUp(credential)
    }
}