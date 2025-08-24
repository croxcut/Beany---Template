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

package com.example.feature.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.remote.supabase.SessionRepository
import com.example.domain.usecase.IsOnboardedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LaunchPageViewModel @Inject constructor(
    private val isCompletedUseCase: IsOnboardedUseCase,
    private val sessionRepository: SessionRepository
): ViewModel() {

    fun isOnboardingCompleted() = isCompletedUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)


}