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
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.detection.viewModel.DetectionViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.composables.InputField
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.data.model.Diagnosis
import com.example.domain.model.db.Note
import com.example.feature.detection.misc.TiltedImage
import com.example.feature.detection.misc.saveBitmapAndGetPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    var showConfirmDialog by remember { mutableStateOf(false) }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

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

    var noteText by remember { mutableStateOf("") }
    val currentDiagnosis by remember(currentIndex, overlayBoxes) {
        derivedStateOf {
            overlayBoxes.maxByOrNull { it.confidence_score }?.class_name ?: "No diagnosis"
        }
    }

    val diagnosisInfo = when (currentDiagnosis) {
        "healthy" -> mapOf(
            "Risk Level" to "Low",
            "Signs And Symptoms" to """
            1. Leaves are vibrant green.
            2. Pods are well-formed and firm.
            3. Plant shows normal growth rate.
            4. No visible disease or pest damage.
            5. Flowers are healthy and abundant.
            6. Stem and branches are strong and flexible.
            7. Soil moisture is adequate.
            8. Overall plant vigor is high.
        """.trimIndent(),
            "Daily Care Guide" to """
            1. Maintain regular watering and nutrient supply.
            2. Monitor for early signs of stress or discoloration.
            3. Ensure adequate sunlight exposure.
            4. Prune lightly to encourage airflow.
            5. Keep the area free of weeds.
            6. Conduct routine inspections for pests.
            7. Fertilize according to growth stage.
            8. Mulch to conserve soil moisture and temperature.
        """.trimIndent()
        )
        "pod-rot" -> mapOf(
            "Risk Level" to "High",
            "Signs And Symptoms" to """
            1. Brown, mushy pods with foul odor.
            2. Mold may develop on pod surface.
            3. Pods may drop prematurely.
            4. Surrounding leaves may yellow or wilt.
            5. Pods may have blackened spots or lesions.
            6. Stem or branches near pods may show rot.
            7. Infection may spread to nearby healthy pods.
            8. Pods may release liquid when pressed.
        """.trimIndent(),
            "Daily Care Guide" to """
            1. Remove affected pods immediately and dispose properly.
            2. Apply appropriate fungicide as per guidelines.
            3. Avoid waterlogging; improve field drainage.
            4. Ensure proper plant spacing for airflow.
            5. Clean tools and equipment after handling infected pods.
            6. Monitor nearby plants for early signs of infection.
            7. Reduce overhead irrigation to minimize wet surfaces.
            8. Rotate crops if possible to break pathogen cycle.
        """.trimIndent()
        )
        "pod-borer" -> mapOf(
            "Risk Level" to "Medium",
            "Signs And Symptoms" to """
            1. Holes in pods with frass around entry points.
            2. Larvae inside pods causing internal damage.
            3. Pods may become deformed or drop prematurely.
            4. Boring activity may spread to multiple pods.
            5. Visible silk or webbing near entry holes.
            6. Pods may smell fermented or decayed.
            7. Nearby leaves may show minor bite marks.
            8. Plant vigor may reduce if infestation is severe.
        """.trimIndent(),
            "Daily Care Guide" to """
            1. Inspect pods daily for early infestation.
            2. Use biological controls like parasitoid wasps.
            3. Apply insecticide if necessary, following guidelines.
            4. Maintain field hygiene to reduce pest habitat.
            5. Remove and destroy heavily infested pods.
            6. Monitor adult moths or beetles for early detection.
            7. Consider pheromone traps to manage populations.
            8. Rotate crops or interplant to reduce pest pressure.
        """.trimIndent()
        )
        "anthracnose-leaf" -> mapOf(
            "Risk Level" to "High",
            "Signs And Symptoms" to """
            1. Dark, sunken lesions on leaves with yellow halo.
            2. Young shoots may die back.
            3. Leaves may curl or drop prematurely.
            4. Infection spreads rapidly in humid conditions.
            5. Stem lesions may appear near infected leaves.
            6. Fruit may also show black spots if disease spreads.
            7. Leaves may exhibit brown streaks or blight.
            8. Plant growth slows and vigor decreases.
        """.trimIndent(),
            "Daily Care Guide" to """
            1. Prune infected leaves and branches.
            2. Apply recommended fungicides regularly.
            3. Ensure good airflow between plants.
            4. Avoid overhead watering to reduce leaf wetness.
            5. Sanitize tools after pruning.
            6. Remove fallen leaves promptly.
            7. Monitor for new lesions frequently.
            8. Use disease-resistant varieties if available.
        """.trimIndent()
        )
        "healthy-cacao-leaf" -> mapOf(
            "Risk Level" to "Low",
            "Signs And Symptoms" to """
            1. Leaves are green, firm, and free from spots.
            2. Veins appear normal and healthy.
            3. Consistent new leaf growth observed.
            4. No pest or disease damage visible.
            5. Leaves have normal shape and size.
            6. Leaf tips are not yellowed or curled.
            7. Stems and petioles are strong and free of lesions.
            8. Plant shows good overall vigor.
        """.trimIndent(),
            "Daily Care Guide" to """
            1. Maintain proper irrigation and fertilization.
            2. Monitor for early signs of pests or deficiencies.
            3. Prune lightly for sunlight penetration and airflow.
            4. Keep field clean to reduce pest habitat.
            5. Mulch to retain soil moisture.
            6. Remove any weak or damaged leaves.
            7. Regularly check for nutrient deficiencies.
            8. Monitor environmental conditions affecting leaf health.
        """.trimIndent()
        )
        "frost-pod" -> mapOf(
            "Risk Level" to "High",
            "Signs And Symptoms" to """
            1. Pods show blackened or water-soaked tips.
            2. Tissue may appear wilted or brittle.
            3. Leaves may show frost damage.
            4. Damage worsens after repeated frost events.
            5. Pods may drop prematurely.
            6. Growth of affected branches slows down.
            7. Flower buds may be damaged or fail to develop.
            8. Plant overall vigor may decrease in frost-prone areas.
        """.trimIndent(),
            "Daily Care Guide" to """
            1. Protect pods with covers or frost cloths.
            2. Avoid planting in frost-prone areas.
            3. Apply mulch to protect root zone.
            4. Consider frost-tolerant varieties if frost recurs.
            5. Monitor weather forecasts to take preventive action.
            6. Irrigate lightly to reduce frost impact on roots.
            7. Remove damaged pods to prevent secondary infections.
            8. Provide windbreaks to reduce frost exposure.
        """.trimIndent()
        )
        "background" -> mapOf(
            "Risk Level" to "None",
            "Signs And Symptoms" to """
            1. No plants detected in the monitored area.
            2. Only soil or background visible.
            3. No leaf or pod observations possible.
            4. No signs of growth or crop activity.
            5. Field appears bare or unplanted.
            6. Soil moisture and color are the only indicators.
            7. No visible pests or disease symptoms.
            8. No flowering or fruiting structures present.
        """.trimIndent(),
            "Daily Care Guide" to """
            1. No action needed.
            2. Consider planting new crops if required.
            3. Prepare soil for future planting.
            4. Monitor soil health and moisture.
            5. Apply compost or fertilizer if needed.
            6. Check for weeds or unwanted vegetation.
            7. Ensure irrigation system is functional.
            8. Plan crop rotation or planting schedule.
        """.trimIndent()
        )
        else -> mapOf(
            "Risk Level" to "Unknown",
            "Signs And Symptoms" to """
            1. No data available.
            2. Visual symptoms cannot be determined.
            3. Unknown risk factors may be present.
            4. No observed growth patterns.
            5. Potential disease or pest presence uncertain.
            6. Condition requires expert evaluation.
            7. Environmental stressors may be unrecognized.
            8. Further monitoring is advised.
        """.trimIndent(),
            "Daily Care Guide" to """
            1. No guidance available.
            2. Further inspection or expert consultation recommended.
            3. Avoid assumptions about plant health.
            4. Maintain standard irrigation and nutrient supply.
            5. Monitor for any abnormal signs daily.
            6. Document observations for reference.
            7. Isolate plants if disease suspected.
            8. Take photos to support diagnosis.
        """.trimIndent()
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

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
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Beige1, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "Previous",
                                    tint = Brown1,
                                    modifier = Modifier.size(rspDp(35.dp))
                                )
                            }
                        }

                        Button(
                            onClick = {
                                showConfirmDialog = true
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Beige1, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Save",
                                    tint = Brown1,
                                    modifier = Modifier.size(rspDp(35.dp))
                                )
                            }
                        }

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
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Beige1, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Next",
                                    tint = Brown1,
                                    modifier = Modifier.size(rspDp(35.dp))
                                )
                            }
                        }
                    }

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
                                textStyle = TextStyle(
                                    color = Brown1,
                                    fontSize = rspSp(16.sp),
                                    fontFamily = GlacialIndifference
                                ),
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

        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                currentBitmap?.let { bitmap ->
                                    val savedPath = saveBitmapAndGetPath(context, bitmap)
                                    val notes = if (noteText.isNotBlank()) listOf(Note(content = noteText)) else emptyList()
                                    val diagnosis = Diagnosis(
                                        imageUri = savedPath,
                                        boxes = overlayBoxes,
                                        lat = null,
                                        long = null,
                                        notes = notes
                                    )
                                    viewModel.addActivity("Diagnosis Saved ${overlayBoxes.size}")
                                    viewModel.saveDiagnosis(diagnosis)
                                    noteText = ""
                                }
                                isLoading = false
                            }
                            showConfirmDialog = false
                        }
                    ) {
                        Text(
                            text = "Confirm",
                            style = TextStyle(
                                color = Brown1,
                                fontFamily = Etna,
                                fontSize = rspSp(16.sp)
                            )
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text(
                            text = "Cancel",
                            style = TextStyle(
                                color = Brown1,
                                fontFamily = Etna,
                                fontSize = rspSp(16.sp)
                            )
                        )
                    }
                },
                title = {
                    Text(
                        text = "Save Diagnosis?",
                        style = TextStyle(
                            color = Brown1,
                            fontFamily = Etna,
                            fontSize = rspSp(18.sp)
                        )
                    )
                },
                text = {
                    Text(
                        text = "Do you want to save this diagnosis?",
                        style = TextStyle(
                            color = Brown1,
                            fontFamily = Etna,
                            fontSize = rspSp(15.sp)
                        )
                    )
                },
                containerColor = Beige1,
                shape = RoundedCornerShape(16.dp)
            )
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


