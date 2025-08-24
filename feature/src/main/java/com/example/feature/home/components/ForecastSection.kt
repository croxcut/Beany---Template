package com.example.feature.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.feature.home.viewmodel.WeatherState

@Composable
fun ForecastSection(state: WeatherState) {
    Text(
        text = "Forecast this week",
        style = TextStyle(
            color = Brown1,
            fontFamily = GlacialIndifferenceBold,
            fontSize = rspSp(20.sp)
        ),
        modifier = Modifier.padding(horizontal = rspDp(20.dp))
    )

    state.forecast?.let { forecast ->
        WeatherForecastList(forecast)
    } ?: run {
        Text(
            text = "Loading forecast...",
            style = TextStyle(
                fontFamily = GlacialIndifference,
                fontSize = rspSp(15.sp),
                color = Brown1
            ),
            modifier = Modifier.padding(rspDp(20.dp))
        )
    }
}