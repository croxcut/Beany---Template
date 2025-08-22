package com.example.domain.model.weather

import com.example.domain.model.weather.DailyForecast

data class WeatherForecast(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val dailyForecasts: List<DailyForecast>
)