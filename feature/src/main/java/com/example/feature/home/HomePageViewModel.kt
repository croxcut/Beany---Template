package com.example.feature.home

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.network.NetworkUtils
import com.example.data.repositoryImpl.AuthRepositoryImpl
import com.example.data.repositoryImpl.local.ActivityRepository
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
import kotlinx.coroutines.flow.combine
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
    private val activityRepository: ActivityRepository,
    private val context: Context
): ViewModel() {

    val activities = activityRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addActivity(activity: String) = viewModelScope.launch {
        activityRepository.insert(activity)
    }

    fun clearAll() = viewModelScope.launch {
        activityRepository.clearAll()
    }

    private val _isOnline = MutableStateFlow(NetworkUtils.isInternetAvailable(context))
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    private val _session = MutableStateFlow<UserSession?>(null)
    val session: StateFlow<UserSession?> = _session.asStateFlow()

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
        viewModelScope.launch {
            activities.collect { list ->
                Log.d("ActivityViewModel", "Activities changed: $list")
            }
        }

        checkConnectivity()
        initializeData()

        // Set up reactive city matching
        setupCityMatching()
    }

    private fun setupCityMatching() {
        viewModelScope.launch {
            // Combine both profile and cities state
            combine(_profile, _state) { profile, state ->
                profile to state.cities
            }.collect { (profile, cities) ->
                // Only match when both profile and cities are available
                if (profile != null && cities.isNotEmpty() && _selectedCity.value == null) {
                    val matchedCity = cities.find {
                        it.name.equals(profile.province, ignoreCase = true)
                    }

                    Log.d("HomePageViewModel", "Profile province: '${profile.province}', Cities: ${cities.size}, Matched: ${matchedCity?.name}")

                    matchedCity?.let { selectCity(it) }
                }
            }
        }
    }

    fun initializeData() {
        viewModelScope.launch {
            if (isOnline.value) {
                try {
                    _session.value = sessionRepository.getCurrentSession()
                    _profile.value = sessionRepository.getUserProfile()
                    loadCities() // Load cities after profile
                } catch (e: Exception) {
                    _state.value = _state.value.copy(
                        error = "Initialization failed: ${e.message}"
                    )
                }
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

    fun dummyActivity() {
        // Your activity code
    }

    fun selectCity(city: City) {
        _selectedCity.value = city
        loadWeather(city.latitude, city.longitude)
    }

    private fun loadCities() {
        val cities = weatherRepository.getCities()

        Log.d("HomePageViewModel", "=== CITIES LIST ===")
        cities.forEachIndexed { index, city ->
            Log.d("HomePageViewModel", "City $index: '${city.name}'")
        }

        _state.value = _state.value.copy(cities = cities)

        // Only set default city if no selection exists AND no profile match will happen
        if (cities.isNotEmpty() && _selectedCity.value == null && _profile.value == null) {
            val cityToSelect = cities.first()
            _selectedCity.value = cityToSelect
            loadWeather(cityToSelect.latitude, cityToSelect.longitude)
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