package com.example.data.repositoryImpl

import com.example.data.local.loadCity
import com.example.data.remote.WeatherApiService
import com.example.data.remote.model.WeatherApiResponse
import com.example.domain.model.City
import com.example.domain.model.DailyForecast
import com.example.domain.model.WeatherForecast
import com.example.domain.repository.WeatherRepository
import javax.inject.Inject
import android.content.Context
import kotlinx.datetime.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val context: Context
) : WeatherRepository {

    override suspend fun getWeeklyForecast(latitude: Double, longitude: Double): WeatherForecast {
        val response = weatherApiService.getWeeklyForecast(latitude, longitude)
        return response.toWeatherForecast()
    }

    override fun getCities(): List<City> {
        return loadCity(context,"city-coord.json")
    }

    private fun WeatherApiResponse.toWeatherForecast(): WeatherForecast {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val dailyForecasts = daily.time.mapIndexed { index, dateStr ->
            val date = dateFormat.parse(dateStr) ?: Date()
            val calendar = Calendar.getInstance().apply { time = date }
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            DailyForecast(
                date = dateStr,
                dayOfWeek = dayOfWeek,
                maxTemperature = daily.temperature_2m_max[index],
                minTemperature = daily.temperature_2m_min[index],
                weatherCode = daily.weather_code[index],
                precipitationSum = daily.precipitation_sum[index],
                windSpeed = daily.wind_speed_10m_max[index]
            )
        }

        // Filter to only include the current week (Sunday to Saturday)
        val today = Calendar.getInstance()
        val currentWeekForecasts = dailyForecasts.filter { forecast ->
            val forecastDate = dateFormat.parse(forecast.date) ?: Date()
            val forecastCalendar = Calendar.getInstance().apply { time = forecastDate }
            isSameWeek(today, forecastCalendar)
        }

        // Sort by day of week (Sunday=1 to Saturday=7)
        val orderedForecasts = currentWeekForecasts.sortedBy { it.dayOfWeek }

        return WeatherForecast(
            latitude = latitude,
            longitude = longitude,
            timezone = timezone,
            dailyForecasts = orderedForecasts
        )
    }

    private fun isSameWeek(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    }

}
