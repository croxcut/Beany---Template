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

package com.example.core.composables


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.core.content.res.ResourcesCompat

@Composable
fun OutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    fillColor: Color,
    strokeColor: Color,
    strokeWidth: Float,
    fontResId: Int
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val heightInDp = with(density) { fontSize.toDp() }

    Canvas(modifier = modifier.height(heightInDp)) {
        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textAlign = android.graphics.Paint.Align.LEFT
                textSize = fontSize.toPx()
                typeface = ResourcesCompat.getFont(context, fontResId)
            }

            paint.style = android.graphics.Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
            paint.color = strokeColor.toArgb()
            canvas.nativeCanvas.drawText(text, 0f, fontSize.toPx(), paint)

            paint.style = android.graphics.Paint.Style.FILL
            paint.color = fillColor.toArgb()
            canvas.nativeCanvas.drawText(text, 0f, fontSize.toPx(), paint)
        }
    }
}