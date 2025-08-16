package com.example.feature.detection.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Surface
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.core.ui.theme.Beige1
import com.example.domain.model.AABB
import com.example.domain.model.Route
import com.example.feature.detection.viewModel.DetectionViewModel
import java.io.OutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.graphics.Canvas
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import com.example.feature.detection.misc.DetectionOverlay
import com.example.feature.detection.misc.saveBitmapWithBoxes
import com.example.feature.detection.misc.startCamera
import com.example.feature.detection.misc.takeHighResSnapshot

@Composable
fun RealtimeDetectionPage(
    viewModel: DetectionViewModel = hiltViewModel()
) {
    val overlayBoxes by viewModel.boxes.collectAsState()
    val inferenceTime by viewModel.inferenceTime.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var analyzerActive by remember { mutableStateOf(true) }
    var imageCaptureRef by remember { mutableStateOf<ImageCapture?>(null) } // high-res capture

    DisposableEffect(Unit) {
        onDispose {
            analyzerActive = false
            cameraExecutor.shutdownNow()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Transparent)) {
        Box(Modifier.weight(1f)) {
            AndroidView(factory = { ctx ->
                PreviewView(ctx).apply {
                    startCamera(
                        context = ctx,
                        lifecycleOwner = lifecycleOwner,
                        previewView = this,
                        onBitmapCaptured = { bitmap ->
                            viewModel.detect(bitmap)
                        },
                        cameraExecutor = cameraExecutor,
                        analyzerActive = analyzerActive,
                        setImageCapture = { capture -> imageCaptureRef = capture }
                    )
                }
            }, modifier = Modifier.fillMaxSize())

            DetectionOverlay(overlayBoxes, Modifier.fillMaxSize())

            Text(
                text = "Inference: ${inferenceTime}ms",
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(
                        alignment = Alignment.BottomCenter
                    )
                    .navigationBarsPadding()
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val capture = imageCaptureRef
                        if (capture != null) {
                            takeHighResSnapshot(context, capture, overlayBoxes)
                        } else {
                            Toast.makeText(context, "Camera not ready", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Save Snapshot")
                }
            }
        }
    }
}

