package com.example.feature.detection.misc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.example.domain.model.AABB
import java.io.OutputStream
import kotlin.collections.forEach

//fun saveBitmapWithBoxes(context: Context, bitmap: Bitmap, boxes: List<AABB>) {
//    val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//    val canvas = Canvas(mutableBitmap)
//
//    val boxPaint = Paint().apply {
//        color = android.graphics.Color.RED
//        style = Paint.Style.STROKE
//        strokeWidth = 8f
//    }
//
//    val textPaint = Paint().apply {
//        color = android.graphics.Color.WHITE
//        textSize = 50f
//        style = Paint.Style.FILL
//        isAntiAlias = true
//    }
//
//    boxes.forEach { box ->
//        val centerX = (box.x1 + box.x2) / 2f
//        val width = (box.x2 - box.x1) * 1.5f
//        val left = (centerX - width / 2f) * bitmap.width
//        val right = (centerX + width / 2f) * bitmap.width
//        val top = box.y1 * bitmap.height
//        val bottom = box.y2 * bitmap.height
//
//        // Draw rectangle
//        canvas.drawRect(left, top, right, bottom, boxPaint)
//
//        // Draw class name above the box
//        canvas.drawText(box.clsName, left, top - 10f, textPaint)
//    }
//
//    val filename = "detection_${System.currentTimeMillis()}.png"
//    val outputStream = context.contentResolver.insert(
//        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//        android.content.ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
//            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
//            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//        }
//    )?.let { uri -> context.contentResolver.openOutputStream(uri) }
//
//    outputStream?.use {
//        mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
//        Toast.makeText(context, "Saved to gallery", Toast.LENGTH_SHORT).show()
//    } ?: Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
//}

fun saveBitmapWithBoxes(context: Context, bitmap: Bitmap, boxes: List<AABB>) {
    // Scale the bitmap to 1920x1080
    val targetWidth = 1920
    val targetHeight = 1080
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    val canvas = Canvas(scaledBitmap)

    val boxPaint = Paint().apply {
        color = android.graphics.Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    val textPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 50f
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    boxes.forEach { box ->
        val centerX = (box.x1 + box.x2) / 2f
        val width = (box.x2 - box.x1) * 1.5f
        val left = (centerX - width / 2f) * targetWidth
        val right = (centerX + width / 2f) * targetWidth
        val top = box.y1 * targetHeight
        val bottom = box.y2 * targetHeight

        canvas.drawRect(left, top, right, bottom, boxPaint)
        canvas.drawText(box.clsName, left, top - 10f, textPaint)
    }

    val filename = "detection_${System.currentTimeMillis()}.png"
    val outputStream = context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        android.content.ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
    )?.let { uri -> context.contentResolver.openOutputStream(uri) }

    outputStream?.use {
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        Toast.makeText(context, "Saved to gallery", Toast.LENGTH_SHORT).show()
    } ?: Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
}

fun takeHighResSnapshot(
    context: Context,
    imageCapture: ImageCapture,
    boxes: List<AABB>
) {
    val filename = "detection_${System.currentTimeMillis()}.png"
    val contentValues = android.content.ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }
    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = output.savedUri ?: return
                Thread {
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, savedUri)
                    val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    val canvas = Canvas(mutableBitmap)

                    val boxPaint = Paint().apply {
                        color = android.graphics.Color.RED
                        style = Paint.Style.STROKE
                        strokeWidth = 8f
                    }

                    val textPaint = Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 50f
                        style = Paint.Style.FILL
                        isAntiAlias = true
                    }

                    boxes.forEach { box ->
                        val centerX = (box.x1 + box.x2) / 2f
                        val width = (box.x2 - box.x1) * 1.5f
                        val left = (centerX - width / 2f) * bitmap.width
                        val right = (centerX + width / 2f) * bitmap.width
                        val top = box.y1 * bitmap.height
                        val bottom = box.y2 * bitmap.height

                        // Draw rectangle
                        canvas.drawRect(left, top, right, bottom, boxPaint)

                        // Draw class name above the box
                        canvas.drawText(box.clsName, left, top - 10f, textPaint)
                    }

                    // Save back to URI
                    context.contentResolver.openOutputStream(savedUri)?.use {
                        mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                    }

                    // Notify on main thread
                    android.os.Handler(context.mainLooper).post {
                        Toast.makeText(context, "Saved to gallery", Toast.LENGTH_SHORT).show()
                    }
                }.start()
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
            }
        }
    )
}