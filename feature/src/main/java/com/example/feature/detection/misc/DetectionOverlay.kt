package com.example.feature.detection.misc

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.Brown1
import com.example.core.utils.rspDp
import com.example.domain.model.ml.AABB
import kotlin.collections.forEach

@Composable
fun DetectionOverlay(
    boxes: List<AABB>,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
    ) {
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

            // Original coordinates scaled to canvas size
            var left = (centerX - width / 2f) * size.width
            var right = (centerX + width / 2f) * size.width
            var top = box.y1 * size.height
            var bottom = box.y2 * size.height

            // Clamp coordinates to canvas bounds
            left = left.coerceIn(0f, size.width)
            right = right.coerceIn(0f, size.width)
            top = top.coerceIn(0f, size.height)
            bottom = bottom.coerceIn(0f, size.height)

            drawContext.canvas.nativeCanvas.drawRect(left, top, right, bottom, boxPaint)

            // Draw text, ensuring it’s not above the canvas
            val textY = (top - 8f).coerceAtLeast(0f)
            drawContext.canvas.nativeCanvas.drawText(box.clsName, left, textY, textPaint)
        }
    }
}