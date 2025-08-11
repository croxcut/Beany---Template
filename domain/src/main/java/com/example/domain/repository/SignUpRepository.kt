package com.example.domain.repository

import com.example.domain.model.SignUpCredential

interface SignUpRepository{
    suspend fun signUp(signUpCredential: SignUpCredential): Result<String>
}