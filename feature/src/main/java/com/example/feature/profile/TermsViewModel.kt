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

package com.example.feature.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Terms
import com.example.domain.usecase.GetTermsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val getTermsUseCase: GetTermsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TermsUiState())
    val uiState: StateFlow<TermsUiState> = _uiState.asStateFlow()

    fun loadTerms() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val data = getTermsUseCase()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    terms = data,
                    showDialog = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun dismissDialog() {
        _uiState.value = _uiState.value.copy(showDialog = false)
    }
}

data class TermsUiState(
    val isLoading: Boolean = false,
    val terms: Terms? = null,
    val error: String? = null,
    val showDialog: Boolean = false
)