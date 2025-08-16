package com.example.feature.detection.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AABB
import com.example.domain.repository.DetectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetectionViewModel @Inject constructor(
    private val repository: DetectionRepository
) : ViewModel() {

    private val _boxes = MutableStateFlow<List<AABB>>(emptyList())
    val boxes: StateFlow<List<AABB>> get() = _boxes

    private val _inferenceTime = MutableStateFlow(0L)
    val inferenceTime: StateFlow<Long> get() = _inferenceTime

    fun detect(bitmap: Bitmap) {
        viewModelScope.launch {
            repository.detect(bitmap) { boundingBoxes, time ->
                _boxes.value = boundingBoxes
                _inferenceTime.value = time
            }
        }
    }
}