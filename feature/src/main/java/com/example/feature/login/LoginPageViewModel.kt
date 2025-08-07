package com.example.feature.login

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.LoginCredential
import com.example.domain.repository.GoogleAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(
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

    }

}