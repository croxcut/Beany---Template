package com.example.domain.repository.remote.supabase

interface GoogleAuthRepository{
    suspend fun signInWithGoogle(idToken: String): Boolean
}