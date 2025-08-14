package com.example.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.core.composables.Footer
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.DailyForecast
import com.example.domain.model.Profile
import com.example.domain.model.Route
import com.example.domain.model.WeatherForecast
import com.example.feature.R
import io.github.jan.supabase.realtime.Column
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController,
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val activityList by viewModel.activityList.collectAsState()
    val state by viewModel.state.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.dummyActivity()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = rememberScrollState()
            )
            .background(
                color = Brown1
            ),
    ) {

        Column(
            modifier = Modifier
                .statusBarsPadding()
                .height(
                    height = rspDp(
                        baseDp = 120.dp
                    )
                )
                .padding(
                    horizontal = rspDp(20.dp)
                )
                .fillMaxHeight(
                    fraction = 0.15f
                ),
//                .background(
//                    color = White
//                )
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Beany",
                style = TextStyle(
                    fontFamily = Kare,
                    color = White,
                    fontSize = rspSp(
                        baseSp = 50.sp
                    )
                )
            )

        }

        // Content: Weather Panel :)
        Column(
            modifier = Modifier
                .height(
                    height = rspDp(
                        baseDp = 400.dp
                    )
                )
                .fillMaxWidth()
//                .padding(
//                    horizontal = rspDp(
//                        baseDp = 10.dp
//                    )
//                )
                .background(
                    color = White,
                    shape = RoundedCornerShape(
                        topStart = rspDp(
                            baseDp = 40.dp
                        ),
                        topEnd = rspDp(
                            baseDp = 40.dp
                        )
                    )
                )
        ) {

            Spacer(modifier = Modifier
                .height(
                    height = rspDp(
                        baseDp = 20.dp
                    )
                )
            )

            // Panel: Day Greeting :)
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = rspDp(
                            baseDp = 30.dp
                        ),
                    )
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Rise and shine, ${profile?.fullName ?: "Guest"}",
                    style = TextStyle(
                        fontFamily = Etna,
                        color = Brown1,
                        fontSize = rspSp(baseSp = 25.sp)
                    )
                )

                Text(
                    text = "what would you like to do today?",
                    style = TextStyle(
                        fontFamily = GlacialIndifference,
                        fontSize = rspSp(
                            baseSp = 15.sp
                        )
                    )
                )
            }

            Column(
                modifier = Modifier
                    .padding(
                        horizontal = rspDp(baseDp = 20.dp),
                        vertical = rspDp(baseDp = 10.dp)
                    )
                    .fillMaxWidth()
                    .height(rspDp(baseDp = 150.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF3F51B5),
                                Color(0xFF2196F3)
                            )
                        ),
                        shape = RoundedCornerShape(rspDp(baseDp = 20.dp))
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var expanded by remember { mutableStateOf(false) }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
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

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = rspDp(15.dp))
                        ) {
                            // Date from state
                            Text(
                                text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                                    .format(Date()),
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    color = Brown1,
                                    fontSize = rspSp(15.sp)
                                ),
                                modifier = Modifier
                                    .offset(y = rspDp(-10.dp))
                            )
                        }

                        val todayMaxTemp = state.forecast?.dailyForecasts
                            ?.find { it.date == SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date()
                            ) }
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

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        val weatherCode = state.forecast?.dailyForecasts
                            ?.find { it.date == SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date()
                            ) }
                            ?.weatherCode

                        val weatherIcon = when(weatherCode) {
                            0 -> "☀️"
                            in 1..3 -> "☁️"
                            in 45..48 -> "🌫️"
                            in 51..67 -> "🌧️"
                            in 71..77 -> "❄️"
                            in 80..82 -> "🌦️"
                            in 95..99 -> "⛈️"
                            else -> "🌤️"
                        }

                        Text(
                            text = weatherIcon,
                            style = TextStyle(
                                fontSize = rspSp(80.sp)
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }

            Text(
                text = "Forecast this week",
                style = TextStyle(
                    color = Brown1,
                    fontFamily = GlacialIndifferenceBold,
                    fontSize = rspSp(20.sp)
                ),
                modifier = Modifier
                    .padding(horizontal = rspDp(20.dp))
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

        Text(
            text = "Get Started with Beany",
            style = TextStyle(
                fontFamily = GlacialIndifferenceBold,
                color = White,
                fontSize = rspSp(
                    baseSp = 18.sp
                )
            ),
            modifier = Modifier
                .padding(
                    all = rspDp(
                        baseDp = 10.dp
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = rspDp(
                        baseDp = 10.dp
                    )
                ),
//                .background(
//                    color = White
//                )
            verticalArrangement = Arrangement.spacedBy(rspDp(baseDp = 10.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(
                            weight = 1f
                        )
                        .optionsContainerConfig()
                        .clickable(
                            onClick = {
                                navController.navigate("Realtime")
                            }
                        )
                ) {
                    Image(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "Template",
                        modifier = Modifier
                            .size(
                                size = rspDp(
                                    baseDp = 80.dp
                                )
                            )
                            .offset(
                                x = rspDp(
                                    baseDp = 80.dp
                                ),
                                y = rspDp(
                                    baseDp = 18.dp
                                )
                            ),
                    )

                    Column(
                        modifier = Modifier
                            .padding(
                                all = rspDp(
                                    baseDp = 10.dp
                                )
                            )
                    ) {
                        Text(
                            text = "Video",
                            style = TextStyle(
                                fontFamily = Etna,
                                color = Brown1,
                                fontSize = rspSp(
                                    baseSp = 20.sp
                                )
                            ),

                        )

                        Text(
                            text = "Realtime Detection",
                            style = TextStyle(
                                fontFamily = GlacialIndifference,
                                color = Brown1
                            ),
                            modifier = Modifier
                                .fillMaxWidth(
                                    fraction = 0.5f
                                )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(
                            weight = 1f
                        )
                        .optionsContainerConfig()
                        .clickable(
                            onClick = {
                                navController.navigate("UploadImage")
                            }
                        )
                ) {
                    Image(
                        painter = painterResource(R.drawable.upload),
                        contentDescription = "Template",
                        modifier = Modifier
                            .size(
                                size = rspDp(
                                    baseDp = 80.dp
                                )
                            )
                            .offset(
                                x = rspDp(
                                    baseDp = 80.dp
                                ),
                                y = rspDp(
                                    baseDp = 18.dp
                                )
                            ),
                    )

                    Column(
                        modifier = Modifier
                            .padding(
                                all = rspDp(
                                    baseDp = 10.dp
                                )
                            )
                    ) {
                        Text(
                            text = "Upload",
                            style = TextStyle(
                                fontFamily = Etna,
                                color = Brown1,
                                fontSize = rspSp(
                                    baseSp = 20.sp
                                )
                            ),

                            )

                        Text(
                            text = "Upload Image for diagnosis",
                            style = TextStyle(
                                fontFamily = GlacialIndifference,
                                color = Brown1
                            ),
                            modifier = Modifier
                                .fillMaxWidth(
                                    fraction = 0.5f
                                )
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(
                            weight = 1f
                        )
                        .optionsContainerConfig()
                        .clickable(
                            onClick = {
                                navController.navigate("SingleImage")
                            }
                        )
                ) {
                    Image(
                        painter = painterResource(R.drawable.diagnosis),
                        contentDescription = "Template",
                        modifier = Modifier
                            .size(
                                size = rspDp(
                                    baseDp = 80.dp
                                )
                            )
                            .offset(
                                x = rspDp(
                                    baseDp = 80.dp
                                ),
                                y = rspDp(
                                    baseDp = 18.dp
                                )
                            ),
                    )

                    Column(
                        modifier = Modifier
                            .padding(
                                all = rspDp(
                                    baseDp = 10.dp
                                )
                            )
                    ) {
                        Text(
                            text = "Camera",
                            style = TextStyle(
                                fontFamily = Etna,
                                color = Brown1,
                                fontSize = rspSp(
                                    baseSp = 20.sp
                                )
                            ),

                            )

                        Text(
                            text = "See your latest Diagnosis!",
                            style = TextStyle(
                                fontFamily = GlacialIndifference,
                                color = Brown1
                            ),
                            modifier = Modifier
                                .fillMaxWidth(
                                    fraction = 0.5f
                                )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(
                            weight = 1f
                        )
                        .optionsContainerConfig()
                ) {
                    Image(
                        painter = painterResource(R.drawable.chat),
                        contentDescription = "Template",
                        modifier = Modifier
                            .size(
                                size = rspDp(
                                    baseDp = 80.dp
                                )
                            )
                            .offset(
                                x = rspDp(
                                    baseDp = 80.dp
                                ),
                                y = rspDp(
                                    baseDp = 22.dp
                                )
                            ),
                    )

                    Column(
                        modifier = Modifier
                            .padding(
                                all = rspDp(
                                    baseDp = 10.dp
                                )
                            )
                    ) {
                        Text(
                            text = "Chat",
                            style = TextStyle(
                                fontFamily = Etna,
                                color = Brown1,
                                fontSize = rspSp(
                                    baseSp = 20.sp
                                )
                            )
                        )

                        Text(
                            text = "Talk with Experts",
                            style = TextStyle(
                                fontFamily = GlacialIndifference,
                                color = Brown1
                            ),
                            modifier = Modifier
                                .fillMaxWidth(
                                    fraction = 0.5f
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(height = rspDp(baseDp = 10.dp)))

        // Activity Status
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = White
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        all = rspDp(
                            baseDp = 10.dp
                        )
                    )
            ) {
                Text(
                    text = "Activity Status",
                    style = TextStyle(
                        fontFamily = Etna,
                        color = Brown1,
                        fontSize = rspSp(
                            baseSp = 20.sp
                        )
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(rspDp(200.dp))
                    .padding(horizontal = rspDp(10.dp))
                    .border(
                        width = rspDp(2.dp),
                        color = Brown1,
                        shape = RoundedCornerShape(rspDp(10.dp))
                    )
                    .padding(rspDp(10.dp))
            ) {
                if (activityList.isEmpty()) {
                    Text(
                        text = "No activity yet :(*",
                        style = TextStyle(
                            fontSize = rspSp(15.sp)
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(activityList) { activity ->
                            Text(
                                text = "Activity: $activity",
                                style = TextStyle(
                                    fontSize = rspSp(15.sp)
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(height = rspDp(baseDp = 50.dp)))

            // Footer Section
            Text(
                text = "Beany",
                style = TextStyle(
                    fontFamily = Kare,
                    fontSize = rspSp(
                        baseSp = 20.sp
                    ),
                    color = Brown1
                )
            )

            Footer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = rspDp(100.dp)),
                onClick = {
                    navController.navigate(Route.AboutUsPage.route)
                }
            )
        }
    }
}

@Composable
fun WeatherForecastList(forecast: WeatherForecast) {
    LazyRow(
        modifier = Modifier
            .height(rspDp(200.dp))
            .padding(
                horizontal = rspDp(20.dp),
                vertical = rspDp(8.dp)
            )
    ) {
        items(forecast.dailyForecasts) { daily ->
            DailyForecastItem(daily = daily)
        }
    }
}

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
            .padding(
                rspDp(2.dp)
            ),
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
            style = TextStyle(
                fontSize = rspSp(50.sp)
            ),
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