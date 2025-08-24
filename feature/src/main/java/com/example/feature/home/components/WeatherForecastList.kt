package com.example.feature.home.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.utils.rspDp
import com.example.domain.model.weather.WeatherForecast

@Composable
fun WeatherForecastList(forecast: WeatherForecast) {
    LazyRow(
        modifier = Modifier
            .height(rspDp(200.dp))
            .padding(
                horizontal = rspDp(20.dp),
                vertical = rspDp(8.dp)
            )
    ) {
        items(forecast.dailyForecasts) { daily ->
            DailyForecastItem(daily = daily)
        }
    }
}