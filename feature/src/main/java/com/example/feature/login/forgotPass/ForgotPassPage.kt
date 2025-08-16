package com.example.feature.login.forgotPass

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.core.composables.InputField
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Route

@Composable
fun ForgotPasswordPage(
    navController: NavController,
    viewModel: PassWordResetViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Brown1)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Optional Back Button
        // Row(
        //     modifier = Modifier.padding(horizontal = 10.dp)
        // ) {
        //     Text(
        //         text = "<",
        //         style = TextStyle(
        //             color = White,
        //             fontSize = rspSp(30.sp),
        //             fontFamily = FontFamily.Serif,
        //             fontWeight = FontWeight.Bold
        //         )
        //     )
        // }

        Spacer(modifier = Modifier.padding(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Change password",
                style = TextStyle(
                    color = White,
                    fontFamily = Kare,
                    fontSize = rspSp(50.sp)
                )
            )
            Text(
                text = "Enter your registered email to get a confirmation link and reset your password.",
                style = TextStyle(
                    color = White,
                    fontFamily = Etna,
                    fontSize = rspSp(16.sp)
                )
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            Text(
                text = "Email Address",
                style = TextStyle(
                    color = White,
                    fontFamily = Etna,
                    fontSize = rspSp(16.sp)
                )
            )
            InputField(
                value = viewModel.userEmail,
                onValueChange = { viewModel.setNewEmail(it) },
                textStyle = TextStyle(
                    fontSize = rspSp(17.sp),
                    fontFamily = GlacialIndifference,
                    color = Color.Gray
                ),
                singleLine = true,
                maxLength = 64,
                modifier = Modifier
                    .height(height = rspDp(53.dp))
                    .fillMaxWidth()
                    .background(
                        color = White,
                        shape = RoundedCornerShape(rspDp(15.dp))
                    )
                    .border(
                        width = rspDp(2.dp),
                        shape = RoundedCornerShape(rspDp(15.dp)),
                        color = Brown1
                    ),
                isPasswordField = false
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    text = "Remember your password? ",
                    style = TextStyle(
                        fontFamily = GlacialIndifference,
                        color = White,
                        fontSize = rspSp(15.sp)
                    )
                )
                Text(
                    text = "Login",
                    style = TextStyle(
                        fontFamily = GlacialIndifferenceBold,
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = rspSp(15.sp)
                    ),
                    modifier = Modifier.clickable {
                        navController.navigate(Route.LoginPage.route) {
                            popUpTo(Route.LoginPage.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            Button(
                onClick = {
                    viewModel.sendEmail()
                    Log.i("Reset-Pass", "User: ${viewModel.userEmail}")
                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = rspDp(40.dp))
                    .background(
                        color = Beige1,
                        shape = RoundedCornerShape(rspDp(40.dp))
                    )
            ) {
                Text(
                    text = "Send",
                    style = TextStyle(
                        fontSize = rspSp(20.sp),
                        fontFamily = GlacialIndifference,
                        color = Brown1
                    )
                )
            }
        }

        Spacer(modifier = Modifier.padding(20.dp))
    }
}
