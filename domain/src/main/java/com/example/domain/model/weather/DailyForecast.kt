package com.example.domain.model.weather

data class DailyForecast(
    val date: String,
    val dayOfWeek: Int,
    val maxTemperature: Double,
    val minTemperature: Double,
    val weatherCode: Int,
    val precipitationSum: Double,
    val windSpeed: Double? = null
)