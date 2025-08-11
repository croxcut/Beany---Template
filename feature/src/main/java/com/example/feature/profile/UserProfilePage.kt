package com.example.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.core.composables.Footer
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Brown2
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Route
import com.example.feature.R

@Composable
fun UserProfilePage(
    viewModel: UserProfileViewModel = hiltViewModel(),
    navController: NavController
) {
    val profile by viewModel.profile.collectAsState()
    val isSignedUp by viewModel.isSignedUp.collectAsState()

    val TOP_SIZE_CLIP: Dp = rspDp(120.dp)
    val DESC_FONT_SIZE: TextUnit = rspSp(12.sp)
    val ROW_PADDING: Dp = rspDp(10.dp)
    val CONTAINER_PADDING: Dp = rspDp(30.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Brown1
            )
            .statusBarsPadding()
            .verticalScroll(
                state = rememberScrollState()
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .padding(vertical = rspDp(10.dp))
        ) {
            Text(
                text = "Beany",
                style = TextStyle(
                    fontFamily = Kare,
                    fontSize = rspSp(50.sp),
                    color = White
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = rspDp(
                            60.dp
                        )
                    )
                    .background(
                        color = Brown2,
                        shape = RoundedCornerShape(
                            topStart = TOP_SIZE_CLIP,
                            topEnd = TOP_SIZE_CLIP
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.padding(vertical = rspDp(40.dp)))

                // Farm
                Text(
                    text = profile?.fullName ?: "Guest",
                    style = TextStyle(
                        fontFamily = GlacialIndifferenceBold,
                        color = White,
                        fontSize = rspSp(28.sp)
                    )
                )

                Spacer(modifier = Modifier.padding(vertical = rspDp(10.dp)))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = profile?.farm ?: "N/A",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            color = White,
                            fontSize = rspSp(14.sp)
                        )
                    )

                    // Address:
                    Text(
                        text = profile?.province ?: "N/A",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            color = White,
                            fontSize = rspSp(14.sp)
                        )
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = rspDp(10.dp)))

                // Account Container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = CONTAINER_PADDING)
                        .background(
                            color = White,
                            shape = RoundedCornerShape(rspDp(20.dp))
                        )
                        .padding(rspDp(15.dp))
                ) {

                    Text(
                        text = "Account",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp),
                            color = Brown1
                        )
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.profile_tile),
                            contentDescription = "Profile-Icon",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "My Profile",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "Edit Name, Password, Farm, Location",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(35.dp))
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.notif_tile),
                            contentDescription = "Profile-Icon",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "Notification",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "Updates From Beany",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(35.dp))
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.png_tile),
                            contentDescription = "Profile-Icon",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "Uploaded Photos",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "Your Uploaded Photos",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(35.dp))
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.table_logo),
                            contentDescription = "Profile-Icon",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "My Diagnosis",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "Check Your Past Diagnosis",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.padding(vertical = rspDp(10.dp)))

                // Feature Container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = CONTAINER_PADDING)
                        .background(
                            color = Beige1,
                            shape = RoundedCornerShape(rspDp(20.dp))
                        )
                        .padding(rspDp(15.dp))
                ) {


                    Text(
                        text = "Feature",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp),
                            color = Brown1
                        )
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.camera_tile),
                            contentDescription = "Camera-tile",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "Camera",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "Scan Cacao Plants with AI!",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(35.dp))
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.scan_history_tile),
                            contentDescription = "Scan-tile",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "Scan History",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "List of Past Scans",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(35.dp))
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.community_tile),
                            contentDescription = "Community-tile",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "Community",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "Your Uploaded Photos",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(35.dp))
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.chat_support_tile),
                            contentDescription = "Chat-Support-tile",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "Chat Support",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "Chat With Our Registered Expers!",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.padding(vertical = rspDp(10.dp)))

                // About Us Container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = CONTAINER_PADDING)
                        .background(
                            color = White,
                            shape = RoundedCornerShape(rspDp(20.dp))
                        )
                        .padding(rspDp(15.dp))
                ) {

                    Text(
                        text = "About Beany",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp),
                            color = Brown1
                        )
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.profile_tile),
                            contentDescription = "Camera-tile",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "About Us",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "Learn about the team behind beany",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(35.dp))
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.terms_and_condition_tile),
                            contentDescription = "Scan-tile",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "Terms and Conditions",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "Our guidelines and user agreement",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(35.dp))
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.privacy_tile),
                            contentDescription = "Community-tile",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "Privacy Policy",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "How we handle and protect your data",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = rspDp(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = rspDp(35.dp))
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = ROW_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.contact_tile),
                            contentDescription = "Chat-Support-tile",
                            modifier = Modifier
                                .size(rspDp(30.dp))
                                .padding(rspDp(4.dp))
                        )

                        Spacer(modifier = Modifier.padding(horizontal = rspDp(2.dp)))

                        Column {

                            Text(
                                text = "Contact Us",
                                style = TextStyle(
                                    fontFamily = GlacialIndifferenceBold,
                                    fontSize = rspSp(15.sp),
                                    color = Brown1
                                )
                            )

                            Text(
                                text = "How to reach our support team",
                                style = TextStyle(
                                    fontFamily = GlacialIndifference,
                                    fontSize = DESC_FONT_SIZE,
                                    color = Brown1
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = rspDp(15.dp)))

                Button(
                    onClick = {
                        if(isSignedUp) {
                            viewModel.logout()
                            navController.navigate(Route.LaunchPage.route) {
                                popUpTo(Route.LaunchPage.route) {
                                    inclusive = true
                                }
                            }
                        } else {
                            navController.navigate(Route.LaunchPage.route) {
                                popUpTo(Route.LaunchPage.route) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .background(
                            color = Color(0xffe8d0a7),
                            shape = RoundedCornerShape(rspDp(20.dp))
                        )
                        .height(rspDp(30.dp))
                        .width(rspDp(200.dp))
                ) {
                    Text(
                        text = if (isSignedUp) "LogOut" else "Sign In",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            color = Brown1
                        )
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = rspDp(15.dp)))

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

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 4.dp,
                            color = Beige1,
                            shape = CircleShape
                        )
                ) {
                    Image(
                        painter = painterResource(R.drawable.paran),
                        contentDescription = "Profile-Picture",
                        modifier = Modifier
                            .padding(rspDp(4.dp))
                            .border(
                                width = 4.dp,
                                shape = CircleShape,
                                color = Brown1
                            )
                            .size(rspDp(110.dp))
                            .clip(
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}