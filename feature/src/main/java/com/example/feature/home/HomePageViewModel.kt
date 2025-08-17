package com.example.feature.home

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.network.NetworkUtils
import com.example.data.repositoryImpl.AuthRepositoryImpl
import com.example.domain.model.City
import com.example.domain.model.Profile
import com.example.domain.model.WeatherForecast
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.SessionRepository
import com.example.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val weatherRepository: WeatherRepository,
    private val authRepository: AuthRepository,
    private val context: Context
): ViewModel() {


    private val _isOnline = MutableStateFlow(NetworkUtils.isInternetAvailable(context))
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    private val _session = MutableStateFlow<UserSession?>(null)
    val session: StateFlow<UserSession?> = _session.asStateFlow()

    // Changed to use stateIn for better flow handling
    val isSignedUp: StateFlow<Boolean> = authRepository.isLoggedIn()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity.asStateFlow()

    init {
        checkConnectivity()
        initializeData()
    }

    fun initializeData() {
        viewModelScope.launch {
            if (isOnline.value) {
                try {
                    _session.value = sessionRepository.getCurrentSession()
                    _profile.value = sessionRepository.getUserProfile()
                } catch (e: Exception) {
                    _state.value = _state.value.copy(
                        error = "Initialization failed: ${e.message}"
                    )
                }
            }
        }
        if(isOnline.value) {
            viewModelScope.launch {
                loadCities()
            }
        }
    }

    fun refreshSession() {
        viewModelScope.launch {
            sessionRepository.updateCurrentSession()
        }
    }

    fun checkConnectivity() {
        _isOnline.value = NetworkUtils.isInternetAvailable(context)
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