package com.example.data.repositoryImpl

import com.example.data.remote.WeatherApiService
import com.example.data.remote.model.WeatherApiResponse
import com.example.domain.model.City
import com.example.domain.model.DailyForecast
import com.example.domain.model.WeatherForecast
import com.example.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
) : WeatherRepository {

    override suspend fun getWeeklyForecast(latitude: Double, longitude: Double): WeatherForecast {
        val response = weatherApiService.getWeeklyForecast(latitude, longitude)
        return response.toWeatherForecast()
    }

    override fun getCities(): List<City> {
        return listOf(
            City(1, "Manila", 14.5995, 120.9842),
            City(2, "Quezon City", 14.6760, 121.0437),
            City(3, "Cebu City", 10.3157, 123.8854),
            City(4, "Davao City", 7.1907, 125.4553),
            City(5, "Baguio", 16.4023, 120.5960),
            City(6, "Iloilo City", 10.7202, 122.5621),
            City(7, "Zamboanga City", 6.9214, 122.0790),
            City(8, "Cagayan de Oro", 8.4542, 124.6319),
            City(9, "Bacolod", 10.6765, 122.9509),
            City(10, "Puerto Princesa", 9.7392, 118.7353)
        )
    }

    private fun WeatherApiResponse.toWeatherForecast(): WeatherForecast {
        val dailyForecasts = daily.time.mapIndexed { index, date ->
            DailyForecast(
                date = date,
                maxTemperature = daily.temperature_2m_max[index],
                minTemperature = daily.temperature_2m_min[index],
                weatherCode = daily.weather_code[index],
                precipitationSum = daily.precipitation_sum[index],
                windSpeed = daily.wind_speed_10m_max[index]
            )
        }

        return WeatherForecast(
            latitude = latitude,
            longitude = longitude,
            timezone = timezone,
            dailyForecasts = dailyForecasts
        )
    }

}