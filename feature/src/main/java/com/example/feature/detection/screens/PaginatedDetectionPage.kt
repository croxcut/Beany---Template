package com.example.feature.detection.screens

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.feature.detection.misc.DetectionOverlay
import com.example.feature.detection.viewModel.DetectionViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.ui.theme.Brown1
import com.example.data.model.Diagnosis
import com.example.domain.model.AABB
import com.example.feature.detection.misc.saveBitmapAndGetPath
import com.example.feature.detection.misc.saveBitmapWithBoxes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

@Composable
fun PaginatedDetectionPage(
    imageUris: List<Uri>,
    viewModel: DetectionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var currentIndex by remember { mutableStateOf(-1) }
    var currentBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val overlayBoxes by viewModel.boxes.collectAsState()
    val inferenceTime by viewModel.inferenceTime.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    // Load first image
    LaunchedEffect(imageUris) {
        if (imageUris.isNotEmpty()) {
            isLoading = true
            val found = loadNextDetectedImage(context, imageUris, viewModel, 0) { bitmap, index ->
                currentBitmap = bitmap
                currentIndex = index
            }
            if (found == null) {
                Toast.makeText(context, "No images with detections", Toast.LENGTH_SHORT).show()
            }
            isLoading = false
        }
    }

    // Compute diagnosis info directly on each recomposition
    val currentDiagnosis = overlayBoxes.maxByOrNull { it.cnf }?.clsName ?: "No diagnosis"
    val diagnosisInfo = when (currentDiagnosis) {
        "healthy" -> mapOf(
            "Risk Level" to "Low",
            "Signs And Symptoms" to "No visible signs of disease.",
            "Daily Care Guide" to "Maintain regular watering and nutrient supply."
        )
        "pod-rot" -> mapOf(
            "Risk Level" to "High",
            "Signs And Symptoms" to "Brown, mushy pods with foul odor.",
            "Daily Care Guide" to "Remove affected pods immediately; apply fungicide."
        )
        "pod-borer" -> mapOf(
            "Risk Level" to "Medium",
            "Signs And Symptoms" to "Holes in pods and frass around entry points.",
            "Daily Care Guide" to "Inspect pods daily; use biological control or insecticide."
        )
        "anthracnose-leaf" -> mapOf(
            "Risk Level" to "High",
            "Signs And Symptoms" to "Dark lesions on leaves with yellow halo.",
            "Daily Care Guide" to "Prune infected leaves; apply fungicide."
        )
        "healthy-cacao-leaf" -> mapOf(
            "Risk Level" to "Low",
            "Signs And Symptoms" to "Leaves appear green and healthy.",
            "Daily Care Guide" to "Maintain proper irrigation and fertilization."
        )
        "frost-pod" -> mapOf(
            "Risk Level" to "High",
            "Signs And Symptoms" to "Pods show blackened tips due to frost.",
            "Daily Care Guide" to "Protect pods with covers; avoid frost-prone areas."
        )
        "background" -> mapOf(
            "Risk Level" to "None",
            "Signs And Symptoms" to "No plants detected.",
            "Daily Care Guide" to "No action needed."
        )
        else -> mapOf(
            "Risk Level" to "Unknown",
            "Signs And Symptoms" to "No data available.",
            "Daily Care Guide" to "No guidance available."
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brown1),
            verticalArrangement = Arrangement.Center
        ) {
            currentBitmap?.let { bitmap ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TiltedImage(bitmap = bitmap, overlayBoxes = overlayBoxes)
                    Text(
                        text = "Inference: ${inferenceTime}ms",
                        color = Color.White,
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.Black.copy(alpha = 0.5f))
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Text(text = "Diagnosis: $currentDiagnosis", color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
                Text(text = "Risk Level: ${diagnosisInfo["Risk Level"]}", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                Text(text = "Signs And Symptoms: ${diagnosisInfo["Signs And Symptoms"]}", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                Text(text = "Daily Care Guide: ${diagnosisInfo["Daily Care Guide"]}", color = Color.White)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Previous Image Button
                Button(
                    onClick = {
                        if (currentIndex > 0) {
                            coroutineScope.launch {
                                isLoading = true
                                val found = loadPreviousDetectedImage(context, imageUris, viewModel, currentIndex - 1) { bitmap, index ->
                                    currentBitmap = bitmap
                                    currentIndex = index
                                }
                                if (found == null) {
                                    Toast.makeText(context, "No previous images with detections", Toast.LENGTH_SHORT).show()
                                }
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading
                ) { Text("Previous") }

                // Next Image Button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            val found = loadNextDetectedImage(context, imageUris, viewModel, currentIndex + 1) { bitmap, index ->
                                currentBitmap = bitmap
                                currentIndex = index
                            }
                            if (found == null) {
                                Toast.makeText(context, "No more images with detections", Toast.LENGTH_SHORT).show()
                            }
                            isLoading = false
                        }
                    },
                    enabled = !isLoading
                ) { Text("Next") }

                // Save Image Button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            currentBitmap?.let { bitmap ->
                                val savedPath = saveBitmapAndGetPath(context, bitmap)
                                val diagnosis = Diagnosis(
                                    imageUri = savedPath,
                                    boxes = overlayBoxes,
                                    lat = null,
                                    long = null
                                )
                                viewModel.saveDiagnosis(diagnosis)
                            }
                            isLoading = false
                        }
                    },
                    enabled = !isLoading
                ) { Text("↓") }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

private suspend fun loadNextDetectedImage(
    context: android.content.Context,
    imageUris: List<Uri>,
    viewModel: DetectionViewModel,
    startIndex: Int = 0,
    onImageFound: (Bitmap, Int) -> Unit
): Boolean? {
    for (i in startIndex until imageUris.size) {
        val bitmap = withContext(Dispatchers.IO) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUris[i])
        }
        viewModel.detect(bitmap)
        var attempts = 0
        while (viewModel.boxes.value.isEmpty() && attempts < 50) {
            kotlinx.coroutines.delay(10)
            attempts++
        }
        if (viewModel.boxes.value.isNotEmpty()) {
            onImageFound(bitmap, i)
            return true
        }
    }
    return null
}

private suspend fun loadPreviousDetectedImage(
    context: android.content.Context,
    imageUris: List<Uri>,
    viewModel: DetectionViewModel,
    startIndex: Int,
    onImageFound: (Bitmap, Int) -> Unit
): Boolean? {
    for (i in startIndex downTo 0) {
        val bitmap = withContext(Dispatchers.IO) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUris[i])
        }
        viewModel.detect(bitmap)
        var attempts = 0
        while (viewModel.boxes.value.isEmpty() && attempts < 50) {
            kotlinx.coroutines.delay(10)
            attempts++
        }
        if (viewModel.boxes.value.isNotEmpty()) {
            onImageFound(bitmap, i)
            return true
        }
    }
    return null
}


@Composable
fun TiltedImage(bitmap: Bitmap?, overlayBoxes: List<AABB>) {
    var rotationAngle by remember { mutableStateOf(0f) }

    LaunchedEffect(bitmap) {
        rotationAngle = Random.nextFloat() * 30f - 15f
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(
                if (bitmap != null) bitmap.width.toFloat() / bitmap.height.toFloat() else 1f
            )
            .graphicsLayer { rotationZ = rotationAngle }
            .background(Color.Red.copy(alpha = 0.2f))
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        DetectionOverlay(overlayBoxes, Modifier.fillMaxSize())
    }
}

