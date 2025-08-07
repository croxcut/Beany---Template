package com.example.data.remote

import com.example.data.remote.model.WeatherApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast")
    suspend fun getWeeklyForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") dailyParams: String = "temperature_2m_max,temperature_2m_min,weather_code,precipitation_sum,wind_speed_10m_max",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 7,
        @Query("past_days") pastDays: Int = 7 // Fetch past days too
    ): WeatherApiResponse
}