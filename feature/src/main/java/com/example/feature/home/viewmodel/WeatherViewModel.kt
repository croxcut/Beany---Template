// ===============================================================================
//
// Copyright (C) 2025-2026 by John Paul Valenzuela
//
// This source is available for distribution and/or modification
// only under the terms of the Beany Source Code License as
// published by Beany. All rights reserved.
//
// The source is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// FITNESS FOR A PARTICULAR PURPOSE. See the Beany Source Code License
// for more details.
//
// ===============================================================================

package com.example.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.weather.City
import com.example.domain.repository.remote.openMeteo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

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
        _state.value = _state.value.copy(cities = weatherRepository.getCities())
    }

    private fun loadWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val forecast = weatherRepository.getWeeklyForecast(latitude, longitude)
                _state.value = _state.value.copy(
                    forecast = forecast,
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

//data class WeatherState(
//    val isLoading: Boolean = false,
//    val error: String? = null,
//    val forecast: WeatherForecast? = null,
//    val cities: List<City> = emptyList()
//)