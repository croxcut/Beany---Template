package com.example.feature.login.forgotPass

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
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brown1)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.padding(40.dp))

        when (state) {
            is PasswordResetState.PendingVerification -> {
                Text(
                    text = "We sent a password reset link to ${viewModel.userEmail}. Please check your inbox.",
                    color = White,
                    modifier = Modifier.padding(20.dp)
                )
            }
            is PasswordResetState.Error -> {
                Text(
                    text = (state as PasswordResetState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(20.dp)
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Change password",
                        color = White,
                        fontFamily = Kare,
                        fontSize = rspSp(50.sp)
                    )
                    Text(
                        text = "Enter your registered email to get a reset link.",
                        color = White,
                        fontFamily = Etna,
                        fontSize = rspSp(16.sp)
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp))

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
                        .height(rspDp(53.dp))
                        .fillMaxWidth()
                        .background(White, RoundedCornerShape(rspDp(15.dp)))
                        .border(rspDp(2.dp), Brown1, RoundedCornerShape(rspDp(15.dp))),
                    isPasswordField = false
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.sendEmail() },
                    colors = ButtonDefaults.buttonColors(containerColor = Beige1),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = rspDp(40.dp))
                        .background(Beige1, RoundedCornerShape(rspDp(40.dp)))
                ) {
                    Text("Send", fontSize = rspSp(20.sp), fontFamily = GlacialIndifference, color = Brown1)
                }
            }
        }
    }
}