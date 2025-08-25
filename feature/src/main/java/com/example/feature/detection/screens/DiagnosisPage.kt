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


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.feature.detection.misc.DetectionOverlay
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DiagnosisPage(
    viewModel: DiagnosisViewModel = hiltViewModel()
) {
    val diagnoses = viewModel.allDiagnoses.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
            .padding(8.dp)
    ) {
        items(diagnoses.value) { diagnosis ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                val bitmap = BitmapFactory.decodeFile(diagnosis.imageUri)
                bitmap?.let {
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

                Spacer(modifier = Modifier.height(8.dp))

                Text("ID: ${diagnosis.id}")
                Text("Image URI: ${diagnosis.imageUri}")
                Text("Latitude: ${diagnosis.lat ?: "N/A"}")
                Text("Longitude: ${diagnosis.long ?: "N/A"}")

                Spacer(modifier = Modifier.height(4.dp))

                Text("Boxes:")
                diagnosis.boxes.forEachIndexed { index, box ->
                    Text("  Box ${index + 1}: ${box.class_name}")
                }

                val formattedDate = diagnosis.diagnosedAt?.let { date ->
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    sdf.format(date)
                } ?: "N/A"

                Text("Date: $formattedDate")

                Spacer(modifier = Modifier.height(8.dp))
                Text("Notes:", style = MaterialTheme.typography.titleSmall)
                if (diagnosis.notes.isEmpty()) {
                    Text("No notes available", style = MaterialTheme.typography.bodySmall)
                } else {
                    Column {
                        diagnosis.notes.forEachIndexed { index, note ->
                            val noteDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                .format(note.createdAt)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Note ${index + 1} ($noteDate):",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = note.content,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = { viewModel.deleteDiagnosis(diagnosis) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Delete")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}