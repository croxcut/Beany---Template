package com.example.domain.repository.remote.openMeteo

import com.example.domain.model.weather.City
import com.example.domain.model.weather.WeatherForecast

interface WeatherRepository {
    suspend fun getWeeklyForecast(latitude: Double, longitude: Double): WeatherForecast
    fun getCities(): List<City>
}