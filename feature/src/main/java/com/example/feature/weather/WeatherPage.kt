package com.example.feature.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.DailyForecast
import com.example.domain.model.WeatherForecast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPage(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Philippines Weather Forecast") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // City Selection Dropdown
            var expanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        value = selectedCity?.name ?: "Select a city",
                        onValueChange = {},
                        label = { Text("City") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        state.cities.forEach { city ->
                            DropdownMenuItem(
                                text = { Text(city.name) },
                                onClick = {
                                    viewModel.selectCity(city)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Loading and Error States
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                state.forecast != null -> {
                    WeatherForecastList(forecast = state.forecast!!)
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Select a city to see the weather forecast")
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherForecastList(forecast: WeatherForecast) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(forecast.dailyForecasts) { daily ->
            DailyForecastItem(daily = daily)
            Divider()
        }
    }
}

@Composable
fun DailyForecastItem(daily: DailyForecast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = daily.date,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Weather icon based on weather code (simplified)
                val weatherIcon = when(daily.weatherCode) {
                    0 -> "☀️" // Clear sky
                    in 1..3 -> "⛅" // Mainly clear, partly cloudy
                    in 45..48 -> "🌫️" // Fog
                    in 51..67 -> "🌧️" // Rain
                    in 71..77 -> "❄️" // Snow
                    in 80..82 -> "🌦️" // Rain showers
                    in 95..99 -> "⛈️" // Thunderstorm
                    else -> "🌤️"
                }

                Text(
                    text = weatherIcon,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Column {
                    Text(
                        text = "Max: ${daily.maxTemperature}°C",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Min: ${daily.minTemperature}°C",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column {
                    Text(
                        text = "Precip: ${daily.precipitationSum}mm",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Wind: ${daily.windSpeed}km/h",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}