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

package com.example.feature.notification

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Route
import com.example.domain.model.ScheduledNotification
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun NotificationPage(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val notifications by viewModel.notifications.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Brown1
            )
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(rspDp(100.dp)),
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Cancel notification",
                        tint = Beige1,
                        modifier = Modifier.size(rspDp(40.dp))
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "NOTIFICATION",
                    style = TextStyle(
                        fontFamily = Kare,
                        fontSize = rspSp(30.sp),
                        color = White
                    ),
                    modifier = Modifier.padding(top = rspDp(15.dp))
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.size(rspDp(40.dp))
                ) {

                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (notifications.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = White
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notifications) { notification ->
                    NotificationCard(
                        notification = notification,
                        onRemove = { viewModel.removeNotification(notification.id) },
                        navController = navController
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No scheduled notifications",
                    style = TextStyle(
                        color = White,
                        fontFamily = Etna,
                        fontSize = rspSp(15.sp)
                    )
                )
            }
        }
    }
}

private fun extractDiagnosisIdFromMessage(message: String): Long {
    return try {
        val pattern = "#(\\d+)".toRegex()
        pattern.find(message)?.groupValues?.get(1)?.toLong() ?: 1L
    } catch (e: Exception) {
        1L
    }
}
@Composable
fun NotificationCard(
    notification: ScheduledNotification,
    onRemove: () -> Unit,
    navController: NavController
) {
    var showDropdown by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = rspDp(6.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Time: ${
                        SimpleDateFormat(
                            "MM_dd_yyyy hh:mm a",
                            Locale.getDefault()
                        ).format(Date(notification.scheduledAt))
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }

            Box {
                IconButton(
                    onClick = { showDropdown = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }

                DropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false },
                    containerColor = Brown1,
                    border = BorderStroke(
                        width = rspDp(2.dp),
                        color = Beige1
                    ),
                    shape = RoundedCornerShape(rspDp(10.dp))
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = Beige1,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "View Details",
                                    style = TextStyle(color = White)
                                )
                            }
                        },
                        onClick = {
                            showDropdown = false
                            try {
                                val diagnosisId = notification.message.toLong()
                                navController.navigate(Route.DiagnosisDetailPage.createRoute(diagnosisId))
                            } catch (e: NumberFormatException) {
                                navController.navigate(Route.DiagnosisListPage.route)
                            }
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Cancel Notification",
                                    style = TextStyle(color = White)
                                )
                            }
                        },
                        onClick = {
                            showDropdown = false
                            showConfirmDialog = true
                        },
                        colors = MenuDefaults.itemColors(
                            trailingIconColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = Brown1,
            titleContentColor = White,
            textContentColor = White,
            title = {
                Text(
                    "ConFirm Cancellation",
                    style = TextStyle(
                        fontFamily = Kare,
                        fontSize = rspSp(18.sp)
                    )
                )
            },
            text = {
                Text(
                    "Are you sure you want to cancel this notification?",
                    style = TextStyle(
                        fontSize = rspSp(14.sp)
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemove()
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Beige1
                    )
                ) {
                    Text(
                        "YES",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(14.sp)
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Beige1
                    )
                ) {
                    Text(
                        "CANCEL",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(14.sp),
                        )
                    )
                }
            }
        )
    }
}