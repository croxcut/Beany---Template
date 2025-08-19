package com.example.feature.notification

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun NotificationPage(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var repeatDaily by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val notifications by viewModel.notifications.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Button to open the scheduling dialog
        Button(onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Schedule New Notification")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === Notification List ===
        Text("Scheduled Notifications:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (notifications.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notifications) { notification ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Title: ${notification.title}")
                            Text("Message: ${notification.message}")
                            Text(
                                "Time: ${
                                    SimpleDateFormat(
                                        "dd/MM/yyyy hh:mm a",
                                        Locale.getDefault()
                                    ).format(Date(notification.scheduledAt))
                                }"
                            )
                        }
                        Button(onClick = { viewModel.removeNotification(notification.id) }) {
                            Text("Cancel")
                        }
                    }
                }
            }
        } else {
            Text("No scheduled notifications")
        }
    }

    // === Dialog for scheduling notification ===
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Schedule Notification") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Date Picker
                    Button(onClick = {
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
                    }) {
                        Text(if (selectedDate.isEmpty()) "Pick Date" else "Date: $selectedDate")
                    }

                    // Time Picker
                    Button(onClick = {
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
                    }) {
                        Text(if (selectedTime.isEmpty()) "Pick Time" else "Time: $selectedTime")
                    }

                    // Repeat Daily Checkbox
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = repeatDaily, onCheckedChange = { repeatDaily = it })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Repeat Daily")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.scheduleNotification(
                        calendar,
                        "Helloo you have a Daily Beany Notif",
                        "1",
                        repeatDaily
                    )
                    showDialog = false
                }) {
                    Text("Schedule")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}