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

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import com.example.data.local.repository.DiagnosisRepository
import com.example.data.model.Diagnosis
import com.example.data.repositoryImpl.local.db.ActivityRepository
import com.example.domain.model.ml.AABB
import com.example.domain.model.db.Note
import com.example.domain.repository.local.ml.DetectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetectionViewModel @Inject constructor(
    private val repository: DetectionRepository,
    private val diagnosisRepository: DiagnosisRepository,
    private val activityRepository: ActivityRepository,
) : ViewModel() {
    private val _currentDiagnosis = mutableStateOf<Diagnosis?>(null)
    val currentDiagnosis: State<Diagnosis?> = _currentDiagnosis
    private val _boxes = MutableStateFlow<List<AABB>>(emptyList())
    val boxes: StateFlow<List<AABB>> get() = _boxes

    private val _inferenceTime = MutableStateFlow(0L)
    val inferenceTime: StateFlow<Long> get() = _inferenceTime

    fun addActivity(activity: String) = viewModelScope.launch {
        Log.i("Saved-Activity:", "$activity")
        activityRepository.insert(activity)
    }
    var latestBitmap: Bitmap? = null
    fun detect(bitmap: Bitmap) {
        latestBitmap = bitmap
        viewModelScope.launch {
            repository.detect(bitmap) { boundingBoxes, time ->
                _boxes.value = boundingBoxes
                _inferenceTime.value = time
            }
        }
    }

    fun addNoteToCurrentDiagnosis(note: Note) {
        _currentDiagnosis.value?.let { current ->
            val updatedNotes = current.notes.toMutableList().apply {
                add(note)
            }
            val updatedDiagnosis = current.copy(notes = updatedNotes)
            _currentDiagnosis.value = updatedDiagnosis
            // Save the updated diagnosis immediately
            saveDiagnosis(updatedDiagnosis)
        }
    }

    fun saveDiagnosis(diagnosis: Diagnosis) {
        viewModelScope.launch {
            // Set the current diagnosis first
            _currentDiagnosis.value = diagnosis
            // Then save to repository
            diagnosisRepository.saveDiagnosis(diagnosis)
        }
    }

}