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

package com.example.feature.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.weather.City
import com.example.feature.home.viewmodel.HomePageViewModel
import com.example.feature.home.viewmodel.WeatherState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityAndDateSection(
    state: WeatherState,
    selectedCity: City?,
    isOnline: Boolean,
    viewModel: HomePageViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth(.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedCity?.name ?: "",
                onValueChange = {},
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontFamily = GlacialIndifferenceBold,
                    color = Brown1,
                    fontSize = rspSp(16.sp)
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = Brown1,
                border = BorderStroke(
                    width = rspDp(2.dp),
                    color = Beige1
                ),
                shape = RoundedCornerShape(rspDp(10.dp))
            ) {
                state.cities.forEach { city ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = city.name,
                                style = TextStyle(
                                    fontSize = rspSp(12.sp),
                                    fontFamily = Etna,
                                    color = White
                                )
                            )
                        },
                        onClick = {
                            if (isOnline) {
                                viewModel.selectCity(city)
                            }
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            trailingIconColor = Color.Transparent
                        )
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = rspDp(15.dp))) {
            Text(
                text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    .format(Date()),
                style = TextStyle(
                    fontFamily = GlacialIndifferenceBold,
                    color = Brown1,
                    fontSize = rspSp(15.sp)
                ),
                modifier = Modifier.offset(y = rspDp(-10.dp))
            )

            val todayMaxTemp = state.forecast?.dailyForecasts
                ?.find { it.date == SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date()) }
                ?.maxTemperature

            Text(
                text = todayMaxTemp?.let { "${it}°C" } ?: "--°C",
                style = TextStyle(
                    fontFamily = GlacialIndifferenceBold,
                    color = Brown1,
                    fontSize = rspSp(40.sp)
                )
            )
        }
    }
}