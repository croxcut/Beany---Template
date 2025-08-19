package com.example.feature.detection.misc

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun saveBitmapAndGetPath(context: Context, bitmap: Bitmap): String {
    val filename = "diagnosis_${System.currentTimeMillis()}.png"
    val file = File(context.filesDir, filename)
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return file.absolutePath
}