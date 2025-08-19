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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Black
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.utils.rspSp
import com.example.data.model.Diagnosis
import com.example.feature.detection.misc.DetectionOverlay
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
@Composable
fun DiagnosisListItem(
    diagnosis: Diagnosis,
    onClick: () -> Unit
) {
    val formattedDate = diagnosis.diagnosedAt?.let { date ->
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    } ?: "No date"

    val classNames = diagnosis.boxes.joinToString(", ") { it.clsName }
    val boxCount = diagnosis.boxes.size

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Beige1
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                    Text(
                        text = "Diagnosis",
                        style = TextStyle(
                            color = Black,
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(17.sp)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = classNames,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = Black,
                            fontFamily = GlacialIndifference,
                            fontSize = rspSp(14.sp)
                        )
                    )
                }
            Column (
                horizontalAlignment = Alignment.End
            ) {
                    Text(
                        text = formattedDate,
                        style = TextStyle(
                            color = Black,
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(15.sp)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$boxCount detected",
                        style = TextStyle(
                            color = Black,
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(15.sp)
                        )
                    )
            }
        }
    }
}