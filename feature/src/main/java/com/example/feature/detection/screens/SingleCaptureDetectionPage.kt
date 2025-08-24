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
import androidx.compose.material3.FloatingActionButton
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
import androidx.navigation.NavController
import com.example.feature.detection.misc.DetectionOverlay
import com.example.feature.detection.misc.saveBitmapWithBoxes
import com.example.feature.detection.misc.startCamera
import com.example.feature.detection.misc.takeHighResSnapshot
import com.example.feature.detection.viewModel.DetectionViewModel
import com.example.feature.R
import java.util.concurrent.Executors
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.White
import com.example.domain.model.Route
import com.example.feature.detection.misc.DetectionOverlay
import com.example.feature.detection.misc.saveBitmapAndGetPath
import com.example.feature.detection.misc.saveBitmapWithBoxes
import com.example.feature.detection.misc.startCamera
import com.example.feature.detection.misc.takeHighResSnapshot
import java.io.File
import kotlin.let


@Composable
fun SingleCaptureDetectionPage(
    navController: NavController,
    viewModel: DetectionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageCaptureRef by remember { mutableStateOf<ImageCapture?>(null) }
    var isLoading by remember { mutableStateOf(false) } // <-- loading state

    val overlayBoxes by viewModel.boxes.collectAsState()
    val inferenceTime by viewModel.inferenceTime.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdownNow()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                            analyzerActive = false,
                            setImageCapture = { capture -> imageCaptureRef = capture }
                        )
                    }
                }, modifier = Modifier.fillMaxSize())

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

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    FloatingActionButton(
                        onClick = {
                            val capture = imageCaptureRef
                            if (capture != null) {
                                isLoading = true // show loading

                                // Fast capture directly to file
                                val photoFile = File(context.filesDir, "diagnosis_${System.currentTimeMillis()}.jpg")
                                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                                capture.takePicture(
                                    outputOptions,
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                            val uri = Uri.fromFile(photoFile)

                                            // Pass captured image URI to next page
                                            navController.currentBackStackEntry?.savedStateHandle?.set("images", listOf(uri))
                                            navController.navigate(Route.PaginatedDetectionPage.route)

                                            isLoading = false // hide loading
                                        }

                                        override fun onError(exception: ImageCaptureException) {
                                            Toast.makeText(context, "Capture failed", Toast.LENGTH_SHORT).show()
                                            isLoading = false // hide loading
                                        }
                                    }
                                )
                            } else {
                                Toast.makeText(context, "Camera not ready", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .background(color = Beige1, shape = CircleShape),
                        containerColor = Color.Transparent,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.camera_tile),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                        )
                    }
                }
            }
        }

        // Loading overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Beige1,
                    strokeWidth = 6.dp,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}