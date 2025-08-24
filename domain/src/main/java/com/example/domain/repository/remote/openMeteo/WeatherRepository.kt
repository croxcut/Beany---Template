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

package com.example.domain.repository.remote.openMeteo

import com.example.domain.model.weather.City
import com.example.domain.model.weather.WeatherForecast

interface WeatherRepository {
    suspend fun getWeeklyForecast(latitude: Double, longitude: Double): WeatherForecast
    fun getCities(): List<City>
}