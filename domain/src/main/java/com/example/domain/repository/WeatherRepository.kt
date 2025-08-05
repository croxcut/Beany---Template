package com.example.domain.repository

import com.example.domain.model.City
import com.example.domain.model.WeatherForecast

interface WeatherRepository {
    suspend fun getWeeklyForecast(latitude: Double, longitude: Double): WeatherForecast
    fun getCities(): List<City>
}