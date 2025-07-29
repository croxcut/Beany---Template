package com.example.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import io.github.jan.supabase.realtime.Column

@Composable
fun HomePage(

) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Brown1
            )
    ) {

        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
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
                    text = "Rise and shine, Faith!",
                    style = TextStyle(
                        fontFamily = Etna,
                        color = Brown1,
                        fontSize = rspSp(
                            baseSp = 25.sp
                        )
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

            // Panel: Current Day [Forecast]
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = rspDp(
                            baseDp = 20.dp
                        ),
                        vertical = rspDp(
                            baseDp = 10.dp
                        )
                    )
                    .fillMaxWidth()
                    .height(
                        height = rspDp(
                            baseDp = 150.dp
                        )
                    )
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(
                                    color = 0xFF3F51B5
                                ),
                                Color(
                                    color = 0xFF2196F3
                                ),
                            )
                        ),
                        shape = RoundedCornerShape(
                            size = rspDp(
                                baseDp = 20.dp
                            )
                        )
                    )
            ) {

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

    }

}