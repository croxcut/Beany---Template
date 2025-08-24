package com.example.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.supabase.Profile
import com.example.domain.model.weather.City
import com.example.feature.home.viewmodel.HomePageViewModel
import com.example.feature.home.viewmodel.WeatherState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSection(
    state: WeatherState,
    selectedCity: City?,
    isOnline: Boolean,
    profile: Profile?,
    viewModel: HomePageViewModel
) {
    Column(
        modifier = Modifier
            .height(rspDp(400.dp))
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(
                    topStart = rspDp(40.dp),
                    topEnd = rspDp(40.dp)
                )
            )
    ) {
        Spacer(modifier = Modifier.height(rspDp(20.dp)))

        Column(modifier = Modifier.padding(horizontal = rspDp(30.dp))) {
            Text(
                text = "Rise and shine, ${profile?.fullName ?: "Guest"}",
                style = TextStyle(
                    fontFamily = Etna,
                    color = Brown1,
                    fontSize = rspSp(25.sp)
                )
            )
            Text(
                text = "what would you like to do today?",
                style = TextStyle(
                    fontFamily = GlacialIndifference,
                    fontSize = rspSp(15.sp)
                )
            )
        }

        WeatherCard(
            state = state,
            selectedCity = selectedCity,
            isOnline = isOnline,
            viewModel = viewModel
        )

        ForecastSection(state = state)
    }
}