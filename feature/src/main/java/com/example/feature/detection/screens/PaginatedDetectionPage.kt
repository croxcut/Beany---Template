package com.example.feature.detection.screens

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.feature.detection.misc.DetectionOverlay
import com.example.feature.detection.viewModel.DetectionViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.feature.detection.misc.saveBitmapWithBoxes

@Composable
fun PaginatedDetectionPage(
    imageUris: List<Uri>,
    viewModel: DetectionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var currentIndex by remember { mutableStateOf(-1) } // Start before first valid image
    var currentBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val overlayBoxes by viewModel.boxes.collectAsState()
    val inferenceTime by viewModel.inferenceTime.collectAsState()

    // Load the first image with detections
    LaunchedEffect(imageUris) {
        if (imageUris.isNotEmpty()) {
            loadNextDetectedImage(context, imageUris, viewModel) { bitmap, index ->
                currentBitmap = bitmap
                currentIndex = index
            } ?: Toast.makeText(context, "No images with detections", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        currentBitmap?.let { bitmap ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(bitmap.width.toFloat() / bitmap.height)
            ) {
                Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize())
                DetectionOverlay(overlayBoxes, Modifier.fillMaxSize())
                Text(
                    text = "Inference: ${inferenceTime}ms",
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(androidx.compose.ui.Alignment.TopStart)
                        .background(Color.Black.copy(alpha = 0.5f))
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                loadNextDetectedImage(context, imageUris, viewModel, startIndex = currentIndex + 1) { bitmap, index ->
                    currentBitmap = bitmap
                    currentIndex = index
                } ?: Toast.makeText(context, "No more images with detections", Toast.LENGTH_SHORT).show()
            }) {
                Text("Next Image")
            }

            Button(onClick = {
                currentBitmap?.let { bitmap ->
                    saveBitmapWithBoxes(context, bitmap, overlayBoxes)
                } ?: Toast.makeText(context, "No image to save", Toast.LENGTH_SHORT).show()
            }) {
                Text("Save Image")
            }
        }
    }
}

/**
 * Finds and loads the next image with detected objects.
 */
private fun loadNextDetectedImage(
    context: android.content.Context,
    imageUris: List<Uri>,
    viewModel: DetectionViewModel,
    startIndex: Int = 0,
    onImageFound: (Bitmap, Int) -> Unit
): Boolean? {
    for (i in startIndex until imageUris.size) {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUris[i])
        viewModel.detect(bitmap)
        if (viewModel.boxes.value.isNotEmpty()) {
            onImageFound(bitmap, i)
            return true
        }
    }
    return null
}
