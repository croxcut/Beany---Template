package com.example.feature.detection.misc

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import com.example.domain.model.AABB
import kotlin.random.Random

@Composable
fun TiltedImage(bitmap: Bitmap?, overlayBoxes: List<AABB>) {
    var rotationAngle by remember { mutableStateOf(0f) }

    LaunchedEffect(bitmap) {
        rotationAngle = Random.nextFloat() * 30f - 15f
    }

    val size_: Float = 0.6f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(
                if (bitmap != null) bitmap.width.toFloat() / bitmap.height.toFloat() else 1f
            )
            .graphicsLayer { rotationZ = rotationAngle },
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(size_),
//                contentScale = ContentScale.Crop
            )
        }
        DetectionOverlay(overlayBoxes, Modifier.fillMaxSize(size_))
    }
}