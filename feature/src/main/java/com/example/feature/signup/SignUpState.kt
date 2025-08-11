package com.example.feature.signup

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object PendingVerification : SignUpState()  // New state
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}