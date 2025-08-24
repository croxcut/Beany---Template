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

package com.example.domain.model.weather

import com.example.domain.model.weather.DailyForecast

data class WeatherForecast(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val dailyForecasts: List<DailyForecast>
)