package com.example.feature.login.forgotPass

sealed class PasswordResetState {
    object Idle : PasswordResetState()
    object Loading : PasswordResetState()
    object PendingVerification : PasswordResetState() // For forgot password
    object Success : PasswordResetState() // For reset password success
    data class Error(val message: String) : PasswordResetState()
}