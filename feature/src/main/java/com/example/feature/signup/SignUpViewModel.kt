package com.example.feature.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.City
import com.example.domain.model.SignUpCredential
import com.example.domain.model.UserCredential
import com.example.domain.repository.WeatherRepository
import com.example.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val weatherRepository: WeatherRepository
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

    var selectedLocation by mutableStateOf<City?>(null)
        private set


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

    fun onFarmChanged(newFarm: String) {
        farm = newFarm
    }

    fun onRegisterAsChanged(newRegisterAs: String) {
        registeredAs = newRegisterAs
    }

    fun getUserTag(): List<String> {
        return listOf(
            "Farmer",
            "Expert",
            "Pathologist",
            "Administrator"
        )
    }

    fun onLocationChanged(location: City) {
        selectedLocation = location
        province = location.name // Or whatever property you want to display
    }

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()


    fun signUp(userCredential: UserCredential) {
        viewModelScope.launch {
            Log.d("SignUp", "Starting sign up for: ${userCredential.email}")
            _signUpState.value = SignUpState.Loading

            signUpUseCase(userCredential)
                .onSuccess {
                    Log.d("SignUp", "Sign up successful for: ${userCredential.email}")
                    _signUpState.value = SignUpState.Success
                }
                .onFailure { e ->
                    Log.e("SignUp", "Sign up failed for ${userCredential.email}: ${e.message}", e)
                    _signUpState.value = SignUpState.Error(e.message ?: "Unknown error")
                }
        }
    }

    fun getLocations() : List<City> {
        return weatherRepository.getCities()
    }

}
