package com.example.feature.detection.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.feature.detection.misc.DetectionOverlay
import com.example.feature.detection.misc.saveBitmapWithBoxes
import com.example.feature.detection.misc.startCamera
import com.example.feature.detection.misc.takeHighResSnapshot
import com.example.feature.detection.viewModel.DetectionViewModel
import java.util.concurrent.Executors

@Composable
fun SingleCaptureDetectionPage(
    viewModel: DetectionViewModel = hiltViewModel()
) {
    val overlayBoxes by viewModel.boxes.collectAsState()
    val inferenceTime by viewModel.inferenceTime.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageCaptureRef by remember { mutableStateOf<ImageCapture?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdownNow()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Box(modifier = Modifier.weight(1f)) {
            AndroidView(factory = { ctx ->
                PreviewView(ctx).apply {
                    startCamera(
                        context = ctx,
                        lifecycleOwner = lifecycleOwner,
                        previewView = this,
                        onBitmapCaptured = { bitmap ->
                            capturedBitmap = bitmap
                            viewModel.detect(bitmap)
                        },
                        cameraExecutor = cameraExecutor,
                        analyzerActive = false, // single capture mode
                        setImageCapture = { capture -> imageCaptureRef = capture }
                    )
                }
            }, modifier = Modifier.fillMaxSize())

            // Overlay for detected boxes
            capturedBitmap?.let {
                DetectionOverlay(overlayBoxes, Modifier.fillMaxSize())
                Text(
                    text = "Inference: ${inferenceTime}ms",
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(Color.Black.copy(alpha = 0.5f))
                )
            }
        }

        // Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                val capture = imageCaptureRef
                if (capture != null) {
                    takeHighResSnapshot(context, capture, overlayBoxes)
                } else {
                    Toast.makeText(context, "Camera not ready", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.padding(16.dp)) {
                Text("Save High-Res Snapshot")
            }
        }
    }
}
