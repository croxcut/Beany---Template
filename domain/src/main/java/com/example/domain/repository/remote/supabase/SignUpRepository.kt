package com.example.domain.repository.remote.supabase

import com.example.domain.model.supabase.SignUpCredential

interface SignUpRepository{
    suspend fun signUp(signUpCredential: SignUpCredential): Result<String>
}