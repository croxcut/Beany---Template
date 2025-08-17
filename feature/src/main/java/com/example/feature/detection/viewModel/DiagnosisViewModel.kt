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