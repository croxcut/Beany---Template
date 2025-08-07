package com.example.feature.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.SignUpCredential
import com.example.domain.model.UserCredential
import com.example.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    var username by mutableStateOf("")
        private set

    var fullName by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var province by mutableStateOf("")
        private set

    var farm by mutableStateOf("")
        private set

    var registeredAs by mutableStateOf("")

    fun onUsernameChanged(newUsername: String) {
        username = newUsername
    }

    fun onFullNameChanged(newFullName: String) {
        fullName = newFullName
    }

    fun onEmailChanged(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword
    }

    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
    }

    fun onProvinceChanged(newProvince: String) {
        province = newProvince
    }

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    fun signUp(userCredential: UserCredential) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            signUpUseCase(userCredential)
                .onSuccess {
                    _signUpState.value = SignUpState.Success
                }
                .onFailure { e ->
                    _signUpState.value = SignUpState.Error(e.message ?: "Unknown error")
                }
        }
    }

}
