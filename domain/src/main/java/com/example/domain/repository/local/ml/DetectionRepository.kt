package com.example.domain.repository.local.ml

import android.graphics.Bitmap
import com.example.domain.model.ml.AABB

interface DetectionRepository {
    fun detect(bitmap: Bitmap, onResult: (List<AABB>, Long) -> Unit)
    fun clear()
    fun isCleared(): Boolean
}