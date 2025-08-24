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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.utils.rspSp
import com.example.feature.home.viewmodel.WeatherState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.ranges.contains

@Composable
fun WeatherIconSection(state: WeatherState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val weatherCode = state.forecast?.dailyForecasts
            ?.find { it.date == SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date()) }
            ?.weatherCode

        val weatherIcon = when(weatherCode) {
            0 -> "☀️"
            in 1..3 -> "☁️"
            in 45..48 -> "🌫️"
            in 51..67 -> "🌧️"
            in 71..77 -> "❄️"
            in 80..82 -> "🌦️"
            in 95..99 -> "⛈️"
            else -> "🌤️"
        }

        Text(
            text = weatherIcon,
            style = TextStyle(fontSize = rspSp(80.sp)),
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}
