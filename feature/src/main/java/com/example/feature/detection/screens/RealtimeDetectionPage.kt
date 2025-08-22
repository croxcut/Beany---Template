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
import com.example.domain.model.ml.AABB
import com.example.domain.model.Route
import com.example.feature.detection.viewModel.DetectionViewModel
import java.io.OutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.graphics.Canvas
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.core.ui.theme.Brown1
import com.example.core.utils.rspDp
import com.example.feature.R
import com.example.feature.detection.misc.DetectionOverlay
import com.example.feature.detection.misc.saveBitmapAndGetPath
import com.example.feature.detection.misc.saveBitmapWithBoxes
import com.example.feature.detection.misc.startCamera
import com.example.feature.detection.misc.takeHighResSnapshot
import java.io.File

@Composable
fun RealtimeDetectionPage(
    navController: NavController,
    viewModel: DetectionViewModel = hiltViewModel()
) {
    val overlayBoxes by viewModel.boxes.collectAsState()
    val inferenceTime by viewModel.inferenceTime.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var analyzerActive by remember { mutableStateOf(true) }
    var imageCaptureRef by remember { mutableStateOf<ImageCapture?>(null) } // high-res capture

    var capturedUris by remember { mutableStateOf(listOf<Uri>()) }

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
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {

                    Button(
                        onClick = {
                            analyzerActive = false
                            cameraExecutor.shutdownNow()

                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(
                                    color = Beige1,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(rspDp(40.dp)),
                                tint = Brown1
                            )
                        }
                    }

                    // Capture button
                    Button(
                        onClick = {
                            // Use the latest bitmap from overlay/preview
                            viewModel.latestBitmap?.let { bitmap ->
                                // Save bitmap to file
                                val filePath = saveBitmapAndGetPath(context, bitmap)
                                val uri = Uri.fromFile(File(filePath))
                                capturedUris = capturedUris + uri // Add to list
                                Toast.makeText(context, "Snapshot captured", Toast.LENGTH_SHORT).show()
                            } ?: run {
                                Toast.makeText(context, "No image available", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(
                                    color = Beige1,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.camera_tile),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(bottom = rspDp(2.dp))
                            )
                        }
                    }

                    // Navigate button
                    Button(
                        onClick = {
                            if (capturedUris.isNotEmpty()) {
                                navController.currentBackStackEntry?.savedStateHandle?.set("images", capturedUris)
                                navController.navigate(Route.PaginatedDetectionPage.route)
                            } else {
                                Toast.makeText(context, "No snapshots captured yet", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(
                                    color = Beige1,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(rspDp(40.dp))
                                    .clickable { navController.popBackStack() },
                                tint = Brown1
                            )
                        }
                    }
                }
            }
        }
    }
}

