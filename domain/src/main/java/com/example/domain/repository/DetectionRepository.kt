package com.example.domain.repository

import android.graphics.Bitmap
import com.example.domain.model.AABB

interface DetectionRepository {
    fun detect(bitmap: Bitmap, onResult: (List<AABB>, Long) -> Unit)
    fun clear()
    fun isCleared(): Boolean
}