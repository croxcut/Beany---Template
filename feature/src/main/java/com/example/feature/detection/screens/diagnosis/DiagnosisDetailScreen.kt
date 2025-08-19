package com.example.feature.detection.screens.diagnosis

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.feature.detection.misc.DetectionOverlay
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diagnosis Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFEFEFEF))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Load bitmap from URI
            val bitmap = BitmapFactory.decodeFile(diagnosis.imageUri)
            bitmap?.let {
                Card(
                    modifier = Modifier.fillMaxWidth()
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

            Spacer(modifier = Modifier.height(16.dp))

            // Diagnosis information
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    val formattedDate = diagnosis.diagnosedAt?.let { date ->
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
                    } ?: "N/A"

                    Text("Date: $formattedDate", style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Location:", style = MaterialTheme.typography.titleSmall)
                    Text("Latitude: ${diagnosis.lat ?: "N/A"}")
                    Text("Longitude: ${diagnosis.long ?: "N/A"}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Detections (${diagnosis.boxes.size}):", style = MaterialTheme.typography.titleSmall)
                    diagnosis.boxes.forEachIndexed { index, box ->
                        Text("• ${box.clsName}")
                    }

                    // Display notes section
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Notes:", style = MaterialTheme.typography.titleSmall)
                    if (diagnosis.notes.isEmpty()) {
                        Text("No notes available", style = MaterialTheme.typography.bodySmall)
                    } else {
                        Column {
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
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Text(
                                            text = note.content,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.deleteDiagnosis(diagnosis) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Delete Diagnosis")
            }
        }
    }
}