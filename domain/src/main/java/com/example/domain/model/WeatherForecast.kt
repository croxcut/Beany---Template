package com.example.domain.model

data class WeatherForecast(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val dailyForecasts: List<DailyForecast>
)

