package com.example.feature.login.forgotPass

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PassWordResetViewModel @Inject constructor(

): ViewModel() {

    var email by mutableStateOf("Enter your Email Address")
        private set

    fun setNewEmail(newEmail: String) {
        email = newEmail
    }

}