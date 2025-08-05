package com.example.domain.model

data class WeatherForecast(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val dailyForecasts: List<DailyForecast>
)

data class DailyForecast(
    val date: String,
    val maxTemperature: Double,
    val minTemperature: Double,
    val weatherCode: Int,
    val precipitationSum: Double,
    val windSpeed: Double
)