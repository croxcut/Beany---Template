package com.example.feature.login

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.supabase.LoginCredential
import com.example.domain.repository.local.datastore.AuthRepository
import com.example.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    var warning: String by mutableStateOf("")

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = authRepository.isLoggedIn()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    var email: String by mutableStateOf("")
        private set

    var password: String by mutableStateOf("")
        private set

    var emailError: Boolean by mutableStateOf(false)
    var passwordError: Boolean by mutableStateOf(false)

    fun setUsernameOnChange(newValue: String) {
        email = newValue
        emailError = false
    }

    fun setPasswordOnChange(newValue: String) {
        password = newValue
        passwordError = false
    }

    fun setWarningOnChange(newValue: String) {
        warning = newValue
    }

    fun login(loginCredential: LoginCredential) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                loginUseCase(loginCredential)
                    .onSuccess {
                        _success.value = true
                        authRepository.setLoggedIn(true)
                        Log.d("LoginPageViewModel", "Login successful")
                    }
                    .onFailure { exception ->
                        _success.value = false
                        warning = exception.message ?: "Login failed"
                        Log.e("LoginPageViewModel", "Login failed", exception)
                    }
            } finally {
                _isLoading.value = false
            }
        }
    }
}