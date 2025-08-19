package com.example.feature.detection.screens.diagnosis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.feature.detection.viewModel.DiagnosisViewModel

@Composable
fun DiagnosisListScreen(
    viewModel: DiagnosisViewModel = hiltViewModel(),
    onDiagnosisClick: (Long) -> Unit
) {
    val diagnoses by viewModel.allDiagnoses.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
            .padding(8.dp)
    ) {
        items(diagnoses) { diagnosis ->
            DiagnosisListItem(
                diagnosis = diagnosis,
                onClick = { onDiagnosisClick(diagnosis.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
