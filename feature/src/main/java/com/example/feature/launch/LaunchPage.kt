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

package com.example.feature.launch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.core.composables.GifLoader
import com.example.core.composables.SvgImage
import com.example.core.ui.theme.BeanyTheme
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Kare
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Route
import com.example.feature.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun LaunchPage(
    viewModel: LaunchPageViewModel,
    navController: NavController
) {
    val completed by viewModel.isOnboardingCompleted().collectAsState()

    LaunchedEffect(Unit) {
        delay(4000L)
        navController.popBackStack()
        navController.navigate(
            route = when {
                completed -> Route.LoginPage.route
                else -> Route.OnboardingPage.route
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Beige1
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GifLoader(
            resId = R.drawable.logo_gif,
            modifier = Modifier
                .size(rspDp(120.dp))
        )

        Spacer(modifier = Modifier.height(rspDp(5.dp)))

        Text(
            text = "Beany",
            style = TextStyle(
                color = Brown1,
                fontFamily = Kare,
                fontSize = rspSp(40.sp)
            )
        )

    }

}

