package com.example.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Route
import com.example.feature.R

@Composable
fun OnboardingPage(
    viewModel: OnboardingPageViewModel,
    navController: NavController
) {

    Box(

    ) {
        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f)
                .background(
                    color = Color.Black
                )
        ) {

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_crpd_1),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(rspDp(120.dp)),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "Beany",
                    style = TextStyle(
                        color = White,
                        fontFamily = Kare,
                        fontSize = rspSp(45.sp)
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Beige1,
                        shape = RoundedCornerShape(
                            topEnd = rspDp(30.dp),
                            topStart = rspDp(30.dp))
                    )
            ) {

                Spacer(modifier = Modifier.height(rspDp(20.dp)))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(rspDp(30.dp))
//                        .background(
//                            color = Color.White
//                        )
                ) {
                    Text(
                        text = "Discover the world of",
                        style = TextStyle(
                            fontFamily = Etna,
                            fontSize = rspSp(22.sp)
                        )
                    )

                    Text(
                        text = "Precision Farming!",
                        style = TextStyle(
                            fontFamily = Etna,
                            fontSize = rspSp(37.sp)
                        )
                    )

                    Spacer(modifier = Modifier.height(rspDp(20.dp)))

                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                "Sed do eiusmod tempor incididunt ut labore et dolore " +
                                "magna aliqua. Ut enim ad minim veniam, quis nostrud " +
                                "exercitation ullamco laboris nisi ut aliquip ex ea commodo" +
                                " consequat. \n",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            lineHeight = rspSp(17.sp),
                            fontSize = rspSp(13.sp)
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            viewModel.completeOnboarding()
                            navController.navigate(Route.AboutUsPage.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Brown1,
                                shape = RoundedCornerShape(
                                    size = rspDp(20.dp)
                                )
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = "Get Started",
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(15.sp)
                            )
                        )
                    }
                }
            }
        }
    }
}