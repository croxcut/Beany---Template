package com.example.feature.detection.misc

import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import com.example.domain.model.AABB
import kotlin.collections.forEach

@Composable
fun DetectionOverlay(
    boxes: List<AABB>,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val boxPaint = Paint().apply {
            color = android.graphics.Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }

        val textPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 40f
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val widthScaleFactor = 1.5f

        boxes.forEach { box ->
            val centerX = (box.x1 + box.x2) / 2f
            val width = (box.x2 - box.x1) * widthScaleFactor
            val left = (centerX - width / 2f) * size.width
            val right = (centerX + width / 2f) * size.width
            val top = box.y1 * size.height
            val bottom = box.y2 * size.height

            drawContext.canvas.nativeCanvas.drawRect(left, top, right, bottom, boxPaint)

            drawContext.canvas.nativeCanvas.drawText(box.clsName, left, top - 8f, textPaint)
        }
    }
}