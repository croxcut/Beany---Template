package com.example.domain.repository

import com.example.domain.model.UserCredential

interface SignUpRepository{
    suspend fun signUp(userCredential: UserCredential): Result<Unit>
}