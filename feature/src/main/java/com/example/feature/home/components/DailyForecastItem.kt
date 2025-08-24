package com.example.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.weather.DailyForecast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DailyForecastItem(daily: DailyForecast) {
    val weekday = when (daily.dayOfWeek) {
        Calendar.SUNDAY -> "SUN"
        Calendar.MONDAY -> "MON"
        Calendar.TUESDAY -> "TUE"
        Calendar.WEDNESDAY -> "WED"
        Calendar.THURSDAY -> "THU"
        Calendar.FRIDAY -> "FRI"
        Calendar.SATURDAY -> "SAT"
        else -> daily.date
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(
                color = Beige1,
                shape = RoundedCornerShape(rspDp(20.dp))
            )
            .padding(rspDp(2.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val weatherIcon = when(daily.weatherCode) {
            0 -> "☀️"
            in 1..3 -> "⛅"
            in 45..48 -> "🌫️"
            in 51..67 -> "🌧️"
            in 71..77 -> "❄️"
            in 80..82 -> "🌦️"
            in 95..99 -> "⛈️"
            else -> "🌤️"
        }

        Text(
            text = weatherIcon,
            style = TextStyle(fontSize = rspSp(50.sp)),
            modifier = Modifier.padding(end = 8.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        val dayOnly = try {
            val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(daily.date)
            SimpleDateFormat("dd", Locale.getDefault()).format(parsedDate ?: daily.date)
        } catch (e: Exception) {
            daily.date
        }

        Text(
            text = dayOnly,
            style = TextStyle(
                fontFamily = GlacialIndifference,
                color = Brown1,
                fontSize = rspSp(20.sp)
            ),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = weekday,
            style = TextStyle(
                fontFamily = GlacialIndifference,
                color = Brown1,
                fontSize = rspSp(20.sp)
            ),
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.width(10.dp))
}