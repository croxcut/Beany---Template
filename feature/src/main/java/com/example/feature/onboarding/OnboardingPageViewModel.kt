package com.example.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.SetOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingPageViewModel @Inject constructor(
    private val setCompletedUseCase: SetOnboardingUseCase
): ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            setCompletedUseCase(true)
        }
    }

}