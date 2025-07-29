package com.example.feature.login

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.LoginCredential
import com.example.domain.repository.GoogleAuthRepository
import com.example.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val repository: GoogleAuthRepository
): ViewModel() {

    var userName: String by mutableStateOf(value = "")
        private set

    var password: String by mutableStateOf(value = "")
        private set

    fun setUsernameOnChange(newValue: String) {
        userName = newValue
    }

    fun setPasswordOnChange(newValue: String) {
        password = newValue
    }

    private val _loginResult = MutableStateFlow<Result<Unit>?>(null)
    val loginResult: StateFlow<Result<Unit>?> = _loginResult

    fun validateUser() {
        viewModelScope.launch {
            val credentials = LoginCredential(userName, password)
            _loginResult.value = loginUseCase(credentials)
        }
    }

    private val _signInResult = MutableStateFlow<Boolean?>(null)
    val signInResult: StateFlow<Boolean?> = _signInResult

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val success = repository.signInWithGoogle(idToken)
            _signInResult.value = success
        }
    }

}