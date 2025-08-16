package com.example.feature.profile.updateProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Profile
import com.example.domain.repository.SessionRepository
import com.example.domain.repository.UpdateProfileRepository
import com.example.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val updateProfileRepository: UpdateProfileRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            sessionRepository.getUserProfile()?.let { profile ->
                _userId.value = profile.id.orEmpty()
                _uiState.value = _uiState.value.copy(profile = profile)
            }
        }
    }

    fun onProfileChange(updated: Profile) {
        _uiState.value = _uiState.value.copy(profile = updated)
    }

    fun updateProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, success = false)
            val result = updateProfileRepository.updateProfile(_uiState.value.profile)
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(isLoading = false, success = true)
            } else {
                _uiState.value.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }
}