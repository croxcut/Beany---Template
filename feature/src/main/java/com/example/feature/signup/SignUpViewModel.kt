package com.example.feature.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.SignUpCredential
import com.example.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var fullName by mutableStateOf("")
        private set
    var province by mutableStateOf("")
        private set
    var farm by mutableStateOf("")
        private set

    var signUpResult by mutableStateOf<Result<Unit>?>(null)
        private set

    fun onUsernameChange(value: String) { username = value }
    fun onPasswordChange(value: String) { password = value }
    fun onFullNameChange(value: String) { fullName = value }
    fun onProvinceChange(value: String) { province = value }
    fun onFarmChange(value: String) { farm = value }

    fun signUp() {
        viewModelScope.launch {
            val credential = SignUpCredential(username, password, fullName, province, farm)

            Log.d("SignUp", "Attempting signup with: $credential")

            signUpResult = signUpUseCase(credential)

            signUpResult!!.onSuccess {
                Log.d("SignUp", "Signup successful!")
            }.onFailure { error ->
                Log.e("SignUp", "Signup failed: ${error.message}", error)
            }
        }
    }
}
