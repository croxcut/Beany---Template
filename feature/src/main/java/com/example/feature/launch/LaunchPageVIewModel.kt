package com.example.feature.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.IsOnboardedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LaunchPageViewModel @Inject constructor(
    private val isCompletedUseCase: IsOnboardedUseCase
): ViewModel() {
    fun isOnboardingCompleted() = isCompletedUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)
}