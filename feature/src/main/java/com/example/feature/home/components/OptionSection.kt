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

package com.example.feature.home.components

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Route
import com.example.feature.R
import com.example.feature.home.misc.OptionBox

@Composable
fun OptionsSection(
    navController: NavController,
    isLoggedIn: Boolean,
    context: Context
) {
    Text(
        text = "Get Started with Beany",
        style = TextStyle(
            fontFamily = GlacialIndifferenceBold,
            color = White,
            fontSize = rspSp(18.sp)
        ),
        modifier = Modifier.padding(all = rspDp(10.dp))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = rspDp(10.dp)),
        verticalArrangement = Arrangement.spacedBy(rspDp(10.dp))
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            OptionBox(
                onClick = { navController.navigate(Route.SingleImageDetectionPage.route) },
                iconRes = R.drawable.camera,
                title = "Camera",
                description = "Single Image Capture"
            )

            val uploadLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetMultipleContents(),
                onResult = { uris ->
                    if (uris.isNotEmpty()) {
                        navController.currentBackStackEntry?.savedStateHandle?.set("images", uris)
                        navController.navigate(Route.PaginatedDetectionPage.route)
                    } else {
                        Toast.makeText(context, "No images selected", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            OptionBox(
                onClick = { uploadLauncher.launch("image/*") },
                iconRes = R.drawable.upload,
                title = "Upload",
                description = "Upload Image for diagnosis"
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            OptionBox(
                onClick = { navController.navigate(Route.DiagnosisListPage.route) },
                iconRes = R.drawable.diagnosis,
                title = "Diagnosis",
                description = "See your latest Diagnosis!"
            )

            OptionBox(
                onClick = {
                    if (isLoggedIn) {
                        navController.navigate(Route.PostsListPage.route)
                    } else {
                        Toast.makeText(context,
                            "Please sign up to access Community",
                            Toast.LENGTH_SHORT).show()
                    }
                },
                iconRes = R.drawable.chat,
                title = "Chat Support",
                description = "Talk with Experts"
            )
        }
    }
}