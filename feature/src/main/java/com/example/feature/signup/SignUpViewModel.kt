// ===============================================================================
//
// Copyright (C) 2025-2026 by John Paul Valenzuela
//
// This source is available for distribution and/or modification
// only under the terms of the Beany Source Code License as
// published by Beany. All rights reserved.
//
// The source is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// FITNESS FOR A PARTICULAR PURPOSE. See the Beany Source Code License
// for more details.
//
// ===============================================================================

package com.example.feature.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.weather.City
import com.example.domain.model.supabase.SignUpCredential
import com.example.domain.model.Terms
import com.example.domain.repository.remote.openMeteo.WeatherRepository
import com.example.domain.usecase.GetTermsUseCase
import com.example.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val weatherRepository: WeatherRepository,
    private val getTerms: GetTermsUseCase
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

    var usernameError by mutableStateOf(false)
    var fullNameError by mutableStateOf(false)
    var emailError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    var confirmPasswordError by mutableStateOf(false)

    var isTermsAccepted by mutableStateOf(false)

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
        )
    }

    fun onLocationChanged(location: City) {
        selectedLocation = location
        province = location.name
    }

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)


    fun signUp(signUpCredential: SignUpCredential) {
        viewModelScope.launch {
            Log.d("SignUp", "Starting sign up for: ${signUpCredential.email}")
            _signUpState.value = SignUpState.Loading

            signUpUseCase(signUpCredential)
                .onSuccess {
                    Log.d("SignUp", "Sign up successful for: ${signUpCredential.email}")
                    _signUpState.value = SignUpState.Success
                }
                .onFailure { e ->
                    Log.e("SignUp", "Sign up failed for ${signUpCredential.email}: ${e.message}", e)
                    _signUpState.value = SignUpState.Error(e.message ?: "Unknown error")
                }
        }
    }

    fun getLocations() : List<City> {
        return weatherRepository.getCities().sortedBy {
            it.name
        }
    }

    var uiState by mutableStateOf(TermsUiState(isLoading = true))
        private set

    suspend fun loadTerms() {
        uiState = uiState.copy(isLoading = true, error = null)
        try {
            val data = getTerms()
            uiState = uiState.copy(isLoading = false, terms = data)
        } catch (e: Exception) {
            uiState = uiState.copy(isLoading = false, error = e.message ?: "Unknown error")
        }
    }

    fun setAccepted(checked: Boolean) {
        uiState = uiState.copy(accepted = checked)
    }

    fun dismissDialog() {
        uiState = uiState.copy(showDialog = false)
    }

}

data class TermsUiState(
    val isLoading: Boolean = false,
    val terms: Terms? = null,
    val error: String? = null,
    val showDialog: Boolean = true,
    val accepted: Boolean = false
)
