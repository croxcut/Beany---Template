package com.example.data.repositoryImpl.local.ml

import android.graphics.Bitmap
import com.example.data.tflite.ModelRunner
import com.example.domain.model.ml.AABB
import com.example.domain.repository.local.ml.DetectionRepository
import javax.inject.Inject

class DetectionRepositoryImpl @Inject constructor(
    private val modelRunner: ModelRunner
) : DetectionRepository {

    init {
        modelRunner.setup()
    }

    override fun detect(bitmap: Bitmap, onResult: (List<AABB>, Long) -> Unit) {
        val result = modelRunner.detect(bitmap)
        if (result != null) {
            onResult(result.first, result.second)
        } else {
            onResult(emptyList(), 0L)
        }
    }

    override fun clear() {
        modelRunner.clear()
    }

    override fun isCleared(): Boolean = modelRunner.isCleared()
}