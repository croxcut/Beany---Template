package com.example.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.core.utils.rspDp
import com.example.domain.model.weather.City
import com.example.feature.home.viewmodel.HomePageViewModel
import com.example.feature.home.viewmodel.WeatherState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherCard(
    state: WeatherState,
    selectedCity: City?,
    isOnline: Boolean,
    viewModel: HomePageViewModel
) {
    Column(
        modifier = Modifier
            .padding(horizontal = rspDp(20.dp), vertical = rspDp(10.dp))
            .fillMaxWidth()
            .height(rspDp(150.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3F51B5), Color(0xFF2196F3))
                ),
                shape = RoundedCornerShape(rspDp(20.dp))
            )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            CityAndDateSection(
                state = state,
                selectedCity = selectedCity,
                isOnline = isOnline,
                viewModel = viewModel
            )

            WeatherIconSection(state = state)
        }
    }
}