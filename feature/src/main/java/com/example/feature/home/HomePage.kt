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

package com.example.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.White
import com.example.feature.home.components.HomeContent
import com.example.feature.home.viewmodel.HomePageViewModel
import kotlinx.coroutines.coroutineScope

@Composable
fun HomePage(
    navController: NavController,
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val activityList by viewModel.activities.collectAsState()
    val state by viewModel.state.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val isLoggedIn by viewModel.isSignedUp.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.checkConnectivity()
        if (isOnline) {
            viewModel.initializeData()
            coroutineScope {
                viewModel.dummyActivity()
                viewModel.refreshSession()
            }
        }
    }

    when {
        !isLoggedIn -> HomeContent(
            navController = navController,
            profile = null,
            activityList = emptyList(),
            state = state,
            selectedCity = null,
            isOnline = isOnline,
            isLoggedIn = false,
            viewModel = viewModel
        )

        isLoggedIn && !isOnline -> HomeContent(
            navController = navController,
            profile = profile,
            activityList = activityList,
            state = state.copy(error = "No internet connection"),
            selectedCity = selectedCity,
            isOnline = false,
            isLoggedIn = true,
            viewModel = viewModel
        )

        isLoggedIn && isOnline && profile == null -> LoadingScreen()

        isLoggedIn && isOnline && profile != null -> HomeContent(
            navController = navController,
            profile = profile,
            activityList = activityList,
            state = state,
            selectedCity = selectedCity,
            isOnline = true,
            isLoggedIn = true,
            viewModel = viewModel
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Brown1),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = White)
    }
}



