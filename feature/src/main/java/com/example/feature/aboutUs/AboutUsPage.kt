package com.example.feature.aboutUs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.composables.LogoCard
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown2
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.Kare
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.feature.R
import com.example.core.composables.OutlinedText

@Composable
fun AboutUsPage(
    viewModel: AboutUsPageViewModel = hiltViewModel()
) {

    val paddingStandard = rspDp(20.dp)
    val logoSize = rspDp(100.dp)
    val edgeClip = rspDp(36.dp)

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = "Background-image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f)
                .background(Color.Black)
        )

        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .background(
                        color = Brown2,
                        shape = RoundedCornerShape(
                            topStart = edgeClip,
                            topEnd = edgeClip
                        )
                    )
                    .alpha(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = paddingStandard)
                ) {
                    OutlinedText(
                        text = "Beany",
                        fontSize = rspSp(30.sp),
                        fillColor = Beige1,
                        strokeColor = Color.White,
                        strokeWidth = 10f,
                        fontResId = R.font.kare,
                    )

                    Spacer(modifier = Modifier.height(paddingStandard / 2))

                    Text(
                        text = "We are Computer Science students \nfrom Our Lady of Fatima University dedicated " +
                                "to creating innovative tech solutions for agriculture. Our app uses AI and " +
                                "image processing to detect diseases in cacao and coffee plants. By empowering " +
                                "farmers with early diagnosis, we aim to support healthier crops and more sustainable farming.",
                        color = Color.White,
                        fontFamily = GlacialIndifference,
                        fontSize = rspSp(14.sp),
                        lineHeight = rspSp(14.sp) * 1.3f,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(paddingStandard))

                HorizontalDivider(
                    thickness = 2.dp,
                    color = Beige1,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )

                Spacer(modifier = Modifier.height(paddingStandard))

                Text(
                    text = "OUR TEAM",
                    color = Color.White,
                    fontFamily = Kare,
                    fontSize = rspSp(35.sp)
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = paddingStandard),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(viewModel.members) { member ->
                            MemberCard(member = member)
                            Spacer(modifier = Modifier.height(paddingStandard / 2))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Brown2, Color.Transparent)
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Brown2)
                                )
                            )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.10f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "ABOUT US",
                    fontFamily = Kare,
                    color = Color.White,
                    fontSize = rspSp(40.sp)
                )

                Spacer(modifier = Modifier.width(paddingStandard / 2))

                LogoCard(
                    imageId = R.drawable.logo_crpd_2,
                    modifier = Modifier.size(logoSize)
                        .border(
                            width = 4.dp,
                            color = Brown2,
                            shape = CircleShape
                        )
                        .padding(all = 4.dp)
                )
            }
        }
    }
}