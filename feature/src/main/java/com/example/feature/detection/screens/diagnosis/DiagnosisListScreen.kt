package com.example.feature.detection.screens.diagnosis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.feature.detection.viewModel.DiagnosisViewModel

@Composable
fun DiagnosisListScreen(
    navController: NavController,
    viewModel: DiagnosisViewModel = hiltViewModel(),
    onDiagnosisClick: (Long) -> Unit
) {
    val diagnoses by viewModel.allDiagnoses.collectAsState()

    var sortOption by remember { mutableStateOf("Date") }
    val dropdownOptions = listOf("Date", "Boxes")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brown1)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = "Back",
            modifier = Modifier
                .size(rspDp(40.dp))
                .clickable { navController.popBackStack() },
            tint = Beige1
        )

        // Title + Dropdown Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(rspDp(100.dp))
                .padding(horizontal = rspDp(20.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Diagnosis",
                style = TextStyle(
                    fontFamily = Etna,
                    fontSize = rspSp(40.sp),
                    color = White
                )
            )

            // Dropdown Menu for Sorting
            var expanded by remember { mutableStateOf(false) }
            Box {
                Text(
                    text = "Sort by: $sortOption",
                    style = TextStyle(
                        color = White,
                        fontFamily = GlacialIndifferenceBold
                    ),
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(8.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = Brown1,
                    border = BorderStroke(
                        width = rspDp(2.dp),
                        color = Beige1
                    ),
                    shape = RoundedCornerShape(rspDp(10.dp))
                ) {
                    dropdownOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    style = TextStyle(
                                        color = White,
                                        fontFamily = GlacialIndifferenceBold
                                    )
                                )
                            },
                            onClick = {
                                sortOption = option
                                expanded = false
                            },
                            colors = MenuDefaults.itemColors(
                                trailingIconColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }

        // Sort diagnoses
        val sortedDiagnoses = when (sortOption) {
            "Boxes" -> diagnoses.sortedByDescending { it.boxes.size }
            else -> diagnoses.sortedByDescending { it.diagnosedAt }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEFEFEF))
                .padding(8.dp)
        ) {
            itemsIndexed(sortedDiagnoses) { index, diagnosis ->
                if (index > 0) {
                    HorizontalDivider(
                        thickness = rspDp(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(20.dp), vertical = rspDp(10.dp)),
                        color = Color.Gray.copy(0.8f)
                    )
                }

                DiagnosisListItem(
                    diagnosis = diagnosis,
                    onClick = { onDiagnosisClick(diagnosis.id) }
                )
            }
        }
    }
}
