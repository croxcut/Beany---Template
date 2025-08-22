package com.example.feature.profile.updateProfile

import com.example.domain.model.supabase.Profile

data class ProfileUiState(
    val profile: Profile = Profile(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
