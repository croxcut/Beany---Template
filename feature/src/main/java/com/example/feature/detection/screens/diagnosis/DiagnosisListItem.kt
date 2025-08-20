package com.example.feature.detection.screens.diagnosis


import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Black
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.White
import com.example.core.utils.rspSp
import com.example.data.model.Diagnosis
import com.example.feature.R
import com.example.feature.notification.NotificationViewModel
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
@Composable
fun DiagnosisListItem(
    diagnosis: Diagnosis,
    onClick: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var repeatDaily by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

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

            // Diagnosis information in the middle
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

            Column(
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

            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier.size(25.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.notif_tile),
                    contentDescription = "Set Reminder",
                    tint = Brown1,
                    modifier = Modifier.size(25.dp)
                )
            }

        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "Schedule Reminder",
                    style = TextStyle(
                        color = Brown1,
                        fontFamily = GlacialIndifferenceBold,
                        fontSize = rspSp(18.sp)
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Date Picker
                    Button(
                        onClick = {
                            val datePicker = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    calendar.set(Calendar.YEAR, year)
                                    calendar.set(Calendar.MONTH, month)
                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                    selectedDate = "$dayOfMonth/${month + 1}/$year"
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                            datePicker.show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Beige1,
                            contentColor = Brown1
                        )
                    ) {
                        Text(
                            if (selectedDate.isEmpty()) "Pick Date" else "Date: $selectedDate",
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(14.sp)
                            )
                        )
                    }

                    // Time Picker
                    Button(
                        onClick = {
                            val timePicker = TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    calendar.set(Calendar.MINUTE, minute)
                                    calendar.set(Calendar.SECOND, 0)
                                    val amPm = if (hourOfDay >= 12) "PM" else "AM"
                                    val hour12 = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
                                    selectedTime = "%02d:%02d %s".format(hour12, minute, amPm)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                false
                            )
                            timePicker.show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Beige1,
                            contentColor = Brown1
                        )
                    ) {
                        Text(
                            if (selectedTime.isEmpty()) "Pick Time" else "Time: $selectedTime",
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(14.sp)
                            )
                        )
                    }

                    // Repeat Daily Checkbox
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = repeatDaily,
                            onCheckedChange = { repeatDaily = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Brown1,
                                uncheckedColor = Brown1.copy(alpha = 0.6f),
                                checkmarkColor = Beige1
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Repeat Daily",
                            style = TextStyle(
                                color = Brown1,
                                fontFamily = GlacialIndifference,
                                fontSize = rspSp(14.sp)
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val title = "Diagnosis Reminder"
                        val message = "${diagnosis.id}"

                        viewModel.scheduleNotification(
                            calendar,
                            title,
                            message,
                            repeatDaily
                        )
                        showDialog = false
                        // Reset selections
                        selectedDate = ""
                        selectedTime = ""
                        repeatDaily = false
                    },
                    enabled = selectedDate.isNotEmpty() && selectedTime.isNotEmpty(), // Disable if date/time not selected
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Brown1,
                        contentColor = White,
                        disabledContainerColor = Brown1.copy(alpha = 0.5f),
                        disabledContentColor = White.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        "Schedule",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(14.sp)
                        )
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDialog = false
                        // Reset selections
                        selectedDate = ""
                        selectedTime = ""
                        repeatDaily = false
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Brown1
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                    )
                ) {
                    Text(
                        "Cancel",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(14.sp)
                        )
                    )
                }
            },
            containerColor = Beige1,
            shape = RoundedCornerShape(16.dp),
            titleContentColor = Brown1,
            textContentColor = Brown1
        )
    }
}