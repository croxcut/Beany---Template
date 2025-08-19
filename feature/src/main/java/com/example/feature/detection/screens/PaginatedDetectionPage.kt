package com.example.feature.detection.screens

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.detection.misc.DetectionOverlay
import com.example.feature.detection.viewModel.DetectionViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.composables.InputField
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.data.model.Diagnosis
import com.example.domain.model.AABB
import com.example.domain.model.Note
import com.example.feature.detection.misc.TiltedImage
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
    var noteText by remember { mutableStateOf("") }
    val currentDiagnosis by remember(currentIndex, overlayBoxes) {
        derivedStateOf {
            overlayBoxes.maxByOrNull { it.cnf }?.clsName ?: "No diagnosis"
        }
    }

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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TiltedImage(bitmap = bitmap, overlayBoxes = overlayBoxes)
                }
            }

            Column(
                modifier = Modifier
                    .verticalScroll(state = rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(White)
                        .padding(10.dp)
                        .border(
                            width = rspDp(2.dp),
                            color = Color.Gray.copy(0.8f),
                            shape = RoundedCornerShape(rspDp(15.dp))
                        )
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Results",
                        style = TextStyle(
                            color = Brown1,
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp)
                        ),
                    )

                    Spacer(modifier = Modifier.padding(vertical = rspDp(20.dp)))

                    Row {
                        Text(
                            text = "Diagnosis:",
                            style = TextStyle(
                                color = Brown1,
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(15.sp)
                            ),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = currentDiagnosis,
                            style = TextStyle(
                                color = if(currentDiagnosis != "Healthy") Color.Red else Color.Green,
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(15.sp)
                            ),
                        )
                    }

                    HorizontalDivider(
                        thickness = rspDp(2.dp),
                        color = Brown1.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(40.dp), vertical = rspDp(10.dp))
                    )

                    Column {
                        Row {
                            Text(
                                text = "Risk Level:",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp)
                                )
                            )

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "${diagnosisInfo["Risk Level"]}",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifference,
                                    fontSize = rspSp(15.sp)
                                ),
                                modifier = Modifier.fillMaxWidth(.85f)
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(2.dp),
                        color = Brown1.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(40.dp), vertical = rspDp(10.dp))
                    )

                    Column {
                        Row {
                            Text(
                                text = "Signs And Symptoms:",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp)
                                )
                            )

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Row(
                           modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "${diagnosisInfo["Signs And Symptoms"]}",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifference,
                                    fontSize = rspSp(15.sp)
                                ),
                                modifier = Modifier.fillMaxWidth(.85f)
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(2.dp),
                        color = Brown1.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(40.dp), vertical = rspDp(10.dp))
                    )

                    Column {
                        Row {
                            Text(
                                text = "Daily Care Guide:",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp)
                                )
                            )

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "${diagnosisInfo["Daily Care Guide"]}",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifference,
                                    fontSize = rspSp(15.sp)
                                ),
                                modifier = Modifier.fillMaxWidth(.85f)
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(2.dp),
                        color = Brown1.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(40.dp), vertical = rspDp(10.dp))
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row{
                            Text(
                                text = "Notes: ",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp)
                                ),
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        viewModel.currentDiagnosis.value?.notes?.forEach { note ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = note.content,
                                    style = TextStyle(
                                        color = Brown1,
                                        fontFamily = GlacialIndifference,
                                        fontSize = rspSp(14.sp)
                                    )
                                )
                                Text(
                                    text = note.createdAt.toString(),
                                    style = TextStyle(
                                        color = Brown1.copy(alpha = 0.7f),
                                        fontFamily = GlacialIndifference,
                                        fontSize = rspSp(12.sp)
                                    )
                                )
                            }
                        }

                        HorizontalDivider(
                            thickness = rspDp(2.dp),
                            color = Brown1.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = rspDp(40.dp), vertical = rspDp(10.dp))
                        )

                        // Add new note section
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                Text(
                                    text = "Add New Notes",
                                    style = TextStyle(
                                        color = Brown1,
                                        fontFamily = GlacialIndifferenceBold,
                                        fontSize = rspSp(15.sp)
                                    ),
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }

                            InputField(
                                value = noteText,
                                onValueChange = { noteText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                singleLine = false,
                                maxLines = 3
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row{
                            Text(
                                text = "Add New Notes",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp)
                                ),
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }

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
                                        // Create note if text exists
                                        val notes = if (noteText.isNotBlank()) {
                                            listOf(Note(content = noteText))
                                        } else {
                                            emptyList()
                                        }
                                        val diagnosis = Diagnosis(
                                            imageUri = savedPath,
                                            boxes = overlayBoxes,
                                            lat = null,
                                            long = null,
                                            notes = notes
                                        )
                                        viewModel.saveDiagnosis(diagnosis)
                                        // Clear note text after saving
                                        noteText = ""
                                    }
                                    isLoading = false
                                }
                            },
                            enabled = !isLoading
                        ) { Text("↓") }
                    }
                }
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


