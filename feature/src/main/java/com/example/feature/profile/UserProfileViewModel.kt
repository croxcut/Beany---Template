package com.example.feature.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CurrentUser
import com.example.domain.model.Profile
import com.example.domain.repository.SessionRepository
import com.example.domain.usecase.GetCurrentSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    private val _isSignedUp = MutableStateFlow(false)
    val isSignedUp: StateFlow<Boolean> = _isSignedUp.asStateFlow()

    init {
        viewModelScope.launch {
            _profile.value = sessionRepository.getUserProfile()
            _isSignedUp.value = sessionRepository.isSignedUp()
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionRepository.clearCurrentSession()
        }
    }

}