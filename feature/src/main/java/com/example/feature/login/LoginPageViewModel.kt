package com.example.feature.login

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.domain.model.LoginCredential
import com.example.domain.model.Route
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.GoogleAuthRepository
import com.example.domain.repository.SessionRepository
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
): ViewModel() {

    var warning: String by mutableStateOf("")

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    val isLoggedIn: StateFlow<Boolean> =
        authRepository.isLoggedIn()
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    var email: String by mutableStateOf(value = "")
        private set

    var password: String by mutableStateOf(value = "")
        private set

    fun setUsernameOnChange(newValue: String) {
        email = newValue
    }

    fun setPasswordOnChange(newValue: String) {
        password = newValue
    }

    fun setWarningOnChange(newValue: String) {
        warning = newValue
    }

    fun login(loginCredential: LoginCredential) {
        viewModelScope.launch {
            loginUseCase(loginCredential)
                .onSuccess {
                    _success.value = true
                    authRepository.setLoggedIn(true)
                    Log.e("Sign In", "Success")
                }
                .onFailure {
                    _success.value = false
                    Log.e("Sign In", "Failed")
                }
        }
    }

}