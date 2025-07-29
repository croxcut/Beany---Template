package com.example.domain.repository

interface GoogleAuthRepository{
    suspend fun signInWithGoogle(idToken: String): Boolean
}