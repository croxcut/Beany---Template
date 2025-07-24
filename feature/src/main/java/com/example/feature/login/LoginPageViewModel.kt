package com.example.feature.login

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(

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

    fun validateUser(): Boolean {
        return userName.equals("username") &&
                password.equals("password")
    }

}