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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.core.composables.Footer
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Route
import com.example.feature.R
import io.github.jan.supabase.realtime.Column

@Composable
fun HomePage(
    navController: NavController
) {

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
                    text = "Rise and shine, [{User}]",
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
                            text = "Single Image Detection",
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
                    .fillMaxSize()
                    .padding(horizontal = rspDp(10.dp))
                    .border(
                        width = rspDp(2.dp),
                        color = Brown1,
                        shape = RoundedCornerShape(rspDp(10.dp))
                    )
                    .padding(rspDp(10.dp))
            ) {
                Text("HELLO")
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