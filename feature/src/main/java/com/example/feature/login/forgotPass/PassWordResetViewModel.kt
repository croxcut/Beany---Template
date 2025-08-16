package com.example.feature.login.forgotPass

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserSessionModel
import com.example.domain.repository.ResetPasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@HiltViewModel
class PassWordResetViewModel @Inject constructor(
    private val resetPasswordRepository: ResetPasswordRepository
): ViewModel() {

    var userEmail by mutableStateOf("")
        private set

    private val _state = MutableStateFlow<PasswordResetState>(PasswordResetState.Idle)
    val state: StateFlow<PasswordResetState> = _state

    fun setNewEmail(email: String) {
        userEmail = email
    }

    fun sendEmail() {
        viewModelScope.launch {
            _state.value = PasswordResetState.Loading
            try {
                resetPasswordRepository.sendResetEmail(
                    email = userEmail,
                    redirectUrl = "beanyapp://password-reset")
                _state.value = PasswordResetState.PendingVerification
            } catch (e: Exception) {
                _state.value = PasswordResetState.Error(e.message ?: "Unknown error")
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun importFromDeepLink(model: UserSessionModel, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                val session = UserSession(
                    accessToken = model.accessToken,
                    tokenType = model.tokenType,
                    refreshToken = model.refreshToken,
                    expiresIn = model.expiresIn,
                    user = null
                )
                resetPasswordRepository.importSession(session)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun changePassword(newPassword: String) {
        viewModelScope.launch {
            _state.value = PasswordResetState.Loading
            try {
                resetPasswordRepository.updatePassword(newPassword)
                _state.value = PasswordResetState.Success
            } catch (e: Exception) {
                _state.value = PasswordResetState.Error(e.message ?: "Unknown error")
            }
        }
    }

}