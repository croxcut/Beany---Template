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

package com.example.feature.detection.misc

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
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
            val centerX = (box.xpos_1 + box.xpos_2) / 2f
            val width = (box.xpos_2 - box.xpos_1) * widthScaleFactor

            var left = (centerX - width / 2f) * size.width
            var right = (centerX + width / 2f) * size.width
            var top = box.ypos_1 * size.height
            var bottom = box.ypos_2 * size.height

            left = left.coerceIn(0f, size.width)
            right = right.coerceIn(0f, size.width)
            top = top.coerceIn(0f, size.height)
            bottom = bottom.coerceIn(0f, size.height)

            drawContext.canvas.nativeCanvas.drawRect(left, top, right, bottom, boxPaint)

            val textY = (top - 8f).coerceAtLeast(0f)
            drawContext.canvas.nativeCanvas.drawText(box.class_name, left, textY, textPaint)
        }
    }
}