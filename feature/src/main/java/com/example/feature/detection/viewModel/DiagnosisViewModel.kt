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

package com.example.feature.detection.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.repository.DiagnosisRepository
import com.example.data.model.Diagnosis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    private val repository: DiagnosisRepository
) : ViewModel() {

    val allDiagnoses: StateFlow<List<Diagnosis>> = repository.getAllDiagnoses()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun saveDiagnosis(diagnosis: Diagnosis) {
        viewModelScope.launch {
            repository.saveDiagnosis(diagnosis)
        }
    }

    fun deleteDiagnosis(diagnosis: Diagnosis) {
        viewModelScope.launch {
            repository.deleteDiagnosis(diagnosis)
        }
    }

}