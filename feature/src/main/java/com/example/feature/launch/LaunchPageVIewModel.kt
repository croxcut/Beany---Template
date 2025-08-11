package com.example.feature.launch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.SessionRepository
import com.example.domain.usecase.IsOnboardedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchPageViewModel @Inject constructor(
    private val isCompletedUseCase: IsOnboardedUseCase,
    private val sessionRepository: SessionRepository
): ViewModel() {

    fun isOnboardingCompleted() = isCompletedUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)


}