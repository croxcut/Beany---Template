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

package com.example.feature.detection.screens.diagnosis

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.feature.detection.viewModel.DiagnosisViewModel
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.feature.detection.misc.DetectionOverlay
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material3.*
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Beige1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisDetailScreen(
    diagnosisId: Long,
    viewModel: DiagnosisViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val diagnoses by viewModel.allDiagnoses.collectAsState()
    val diagnosis = diagnoses.find { it.id == diagnosisId }

    if (diagnosis == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Diagnosis not found")
        }
        return
    }

    val currentDiagnosis = diagnosis.boxes.maxByOrNull { it.confidence_score }?.class_name ?: "No diagnosis"

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Diagnosis Details",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Brown1,
                    titleContentColor = White,
                    navigationIconContentColor = White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brown1)
                .verticalScroll(rememberScrollState())
        ) {
            val bitmap = BitmapFactory.decodeFile(diagnosis.imageUri)
            bitmap?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(it.width.toFloat() / it.height.toFloat())
                    ) {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                        DetectionOverlay(diagnosis.boxes, Modifier.fillMaxSize())
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(all = rspDp(10.dp))
                    .border(
                        width = rspDp(2.dp),
                        color = Color.Gray.copy(0.8f),
                        shape = RoundedCornerShape(rspDp(15.dp))
                    )
                    .padding(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column(
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

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                color = if (currentDiagnosis != "healthy") Color.Red else Color.Green,
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

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                text = diagnosisInfo["Risk Level"] ?: "Unknown",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifference,
                                    fontSize = rspSp(15.sp)
                                ),
                                modifier = Modifier.fillMaxWidth(0.85f)
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                text = diagnosisInfo["Signs And Symptoms"] ?: "No data available",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifference,
                                    fontSize = rspSp(15.sp)
                                ),
                                modifier = Modifier.fillMaxWidth(0.85f)
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                text = diagnosisInfo["Daily Care Guide"] ?: "No guidance available",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifference,
                                    fontSize = rspSp(15.sp)
                                ),
                                modifier = Modifier.fillMaxWidth(0.85f)
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            Text(
                                text = "Date:",
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
                                text = diagnosis.diagnosedAt?.let { date ->
                                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
                                } ?: "N/A",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifference,
                                    fontSize = rspSp(15.sp)
                                ),
                                modifier = Modifier.fillMaxWidth(0.85f)
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            Text(
                                text = "Location:",
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
                            Column(
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                Text(
                                    text = "Latitude: ${diagnosis.lat ?: "N/A"}",
                                    style = TextStyle(
                                        color = Brown1,
                                        fontFamily = GlacialIndifference,
                                        fontSize = rspSp(15.sp)
                                    )
                                )
                                Text(
                                    text = "Longitude: ${diagnosis.long ?: "N/A"}",
                                    style = TextStyle(
                                        color = Brown1,
                                        fontFamily = GlacialIndifference,
                                        fontSize = rspSp(15.sp)
                                    )
                                )
                            }
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            Text(
                                text = "Notes:",
                                style = TextStyle(
                                    color = Brown1,
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp)
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        if (diagnosis.notes.isEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "No notes available",
                                    style = TextStyle(
                                        color = Brown1,
                                        fontFamily = GlacialIndifference,
                                        fontSize = rspSp(15.sp)
                                    ),
                                    modifier = Modifier.fillMaxWidth(0.85f)
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                diagnosis.notes.forEachIndexed { index, note ->
                                    val noteDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                        .format(note.createdAt)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            Text(
                                                text = "Note ${index + 1} ($noteDate):",
                                                style = TextStyle(
                                                    color = Brown1,
                                                    fontFamily = GlacialIndifferenceBold,
                                                    fontSize = rspSp(14.sp)
                                                )
                                            )
                                            Text(
                                                text = note.content,
                                                style = TextStyle(
                                                    color = Brown1,
                                                    fontFamily = GlacialIndifference,
                                                    fontSize = rspSp(14.sp)
                                                ),
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(2.dp),
                        color = Brown1.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(40.dp), vertical = rspDp(10.dp))
                    )
                    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color.Red,
                                    shape = CircleShape
                                )
                                .size(rspDp(40.dp))
                                .clickable {
                                    showDeleteConfirmDialog = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Diagnosis",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(rspDp(8.dp)),
                                tint = White
                            )
                        }
                    }

                    if (showDeleteConfirmDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteConfirmDialog = false },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        // Delete the diagnosis and navigate back
                                        viewModel.deleteDiagnosis(diagnosis)
                                        onBackClick()
                                        showDeleteConfirmDialog = false
                                    }
                                ) {
                                    Text(
                                        text = "Delete",
                                        style = TextStyle(
                                            color = Brown1,
                                            fontFamily = GlacialIndifferenceBold,
                                            fontSize = rspSp(16.sp)
                                        )
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showDeleteConfirmDialog = false }
                                ) {
                                    Text(
                                        text = "Cancel",
                                        style = TextStyle(
                                            color = Brown1,
                                            fontFamily = GlacialIndifferenceBold,
                                            fontSize = rspSp(16.sp)
                                        )
                                    )
                                }
                            },
                            title = {
                                Text(
                                    text = "Confirm Delete",
                                    style = TextStyle(
                                        color = Brown1,
                                        fontFamily = GlacialIndifferenceBold,
                                        fontSize = rspSp(18.sp)
                                    )
                                )
                            },
                            text = {
                                Text(
                                    text = "Are you sure you want to delete this diagnosis? This action cannot be undone.",
                                    style = TextStyle(
                                        color = Brown1,
                                        fontFamily = GlacialIndifferenceBold,
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
        }
    }
}