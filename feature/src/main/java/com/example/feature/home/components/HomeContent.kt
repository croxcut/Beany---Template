package com.example.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.core.ui.theme.Brown1
import com.example.data.model.ActivityEntity
import com.example.domain.model.supabase.Profile
import com.example.domain.model.weather.City
import com.example.feature.home.viewmodel.HomePageViewModel
import com.example.feature.home.viewmodel.WeatherState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    navController: NavController,
    profile: Profile?,
    activityList: List<ActivityEntity>,
    state: WeatherState,
    selectedCity: City?,
    isOnline: Boolean,
    isLoggedIn: Boolean,
    viewModel: HomePageViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Brown1),
    ) {
        HeaderSection()

        WeatherSection(
            state = state,
            selectedCity = selectedCity,
            isOnline = isOnline,
            profile = profile,
            viewModel = viewModel
        )

        OptionsSection(
            navController = navController,
            isLoggedIn = isLoggedIn,
            context = context
        )

        ActivityStatusSection(
            activityList = activityList,
            viewModel = viewModel,
            navController = navController
        )

    }
}