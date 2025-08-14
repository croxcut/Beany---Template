package com.example.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.City
import com.example.domain.model.Profile
import com.example.domain.model.WeatherForecast
import com.example.domain.repository.SessionRepository
import com.example.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val weatherRepository: WeatherRepository
): ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    init {
        viewModelScope.launch {
            _profile.value = sessionRepository.getUserProfile()
        }
    }

    private val _activityList = MutableStateFlow<List<String>>(emptyList())
    val activityList: StateFlow<List<String>> = _activityList

    fun dummyActivity() {
//        val randomActivities = listOf(
//            "Running",
//            "Swimming",
//            "Cycling",
//            "Hiking",
//            "Reading",
//            "Cooking",
//            "Painting",
//            "Coding",
//            "Gaming",
//            "Yoga"
//        )
//
//        _activityList.value = randomActivities
    }

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity.asStateFlow()

    init {
        loadCities()
    }

    fun selectCity(city: City) {
        _selectedCity.value = city
        loadWeather(city.latitude, city.longitude)
    }

    private fun loadCities() {
        val cities = weatherRepository.getCities()
        _state.value = _state.value.copy(cities = cities)

        if (cities.isNotEmpty() && _selectedCity.value == null) {
            val firstCity = cities.first()
            _selectedCity.value = firstCity
            loadWeather(firstCity.latitude, firstCity.longitude)
        }
    }

    private fun loadWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val forecast = weatherRepository.getWeeklyForecast(latitude, longitude)

                val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date())

                val todayForecast = forecast.dailyForecasts.find { it.date == todayDate }

                val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    .format(Date())

                val todayWeatherDisplay = todayForecast?.let {
                    "$formattedDate - Max Temp: ${it.maxTemperature}°C"
                }

                _state.value = _state.value.copy(
                    forecast = forecast,
                    todayWeather = todayWeatherDisplay,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "An error occurred",
                    isLoading = false
                )
            }
        }
    }

}

data class WeatherState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val forecast: WeatherForecast? = null,
    val cities: List<City> = emptyList(),
    val todayWeather: String? = null
)