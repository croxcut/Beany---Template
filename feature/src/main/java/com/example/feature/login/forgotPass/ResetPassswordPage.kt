package com.example.feature.login.forgotPass

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.core.composables.InputField
import com.example.core.ui.theme.*
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.UserSessionModel
import com.example.domain.model.Route

@Composable
fun ResetPasswordPage(
    navController: NavController,
    deepLinkUri: Uri?,
    viewModel: PassWordResetViewModel = hiltViewModel()
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    // Parse deep link params
    val params = remember(deepLinkUri) {
        val raw = deepLinkUri?.toString().orEmpty()
        raw.substringAfter("#", "")
            .split("&")
            .mapNotNull {
                val p = it.split("=", limit = 2)
                if (p.size == 2) p[0] to p[1] else null
            }.toMap()
    }

    val accessToken = params["access_token"]
    val type = params["type"]

    LaunchedEffect(accessToken, type) {
        if (!accessToken.isNullOrEmpty() && type == "recovery") {
            val model = UserSessionModel(
                accessToken = accessToken,
                tokenType = params["token_type"] ?: "bearer",
                refreshToken = params["refresh_token"] ?: "",
                expiresIn = params["expires_in"]?.toLongOrNull() ?: 3600L
            )
            viewModel.importFromDeepLink(model) { err ->
                println("❌ Failed to import session: ${err.message}")
            }
        }
    }

    LaunchedEffect(state) {
        if (state is PasswordResetState.Success) {
            navController.navigate(Route.LoginPage.route) {
                popUpTo(0)  // Pop everything from the back stack
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brown1)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                text = "Password must be at least 8 characters long, containing at least one uppercase letter, at least one lowercase letter, at least one number, and at least one special character. It must not match any of your previous passwords.",
                style = TextStyle(
                    color = White,
                    fontFamily = Etna,
                    fontSize = rspSp(14.sp)
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = rspDp(40.dp))
        ) {
            Text(
                text = "New Password",
                style = TextStyle(
                    color = White,
                    fontFamily = Etna,
                    fontSize = rspSp(16.sp)
                )
            )
            InputField(
                value = newPassword,
                onValueChange = { newPassword = it },
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
                    .border(
                        width = rspDp(2.dp),
                        color = Beige1,
                        shape = RoundedCornerShape(rspDp(15.dp))
                    ),
                isPasswordField = true
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = rspDp(40.dp))
        ) {
            Text(
                text = "Confirm New Password",
                style = TextStyle(
                    color = White,
                    fontFamily = Etna,
                    fontSize = rspSp(16.sp)
                )
            )
            InputField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
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
                    .border(
                        width = rspDp(2.dp),
                        color = Beige1,
                        shape = RoundedCornerShape(rspDp(15.dp))
                    ),
                isPasswordField = true
            )
        }

        Spacer(modifier = Modifier.weight(1f))

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

        Spacer(modifier = Modifier.padding(5.dp))

        Button(
            onClick = {
                if (newPassword != confirmPassword || newPassword.length < 6) {
                    println("⚠ Passwords do not match or too short")
                    return@Button
                }
                viewModel.changePassword(newPassword)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Beige1),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = rspDp(40.dp))
                .background(
                    color = Beige1,
                    shape = RoundedCornerShape(rspDp(40.dp))
                )
        ) {
            Text(
                text = "Change Password",
                style = TextStyle(
                    fontSize = rspSp(20.sp),
                    fontFamily = GlacialIndifference,
                    color = Brown1
                )
            )
        }

        Spacer(modifier = Modifier.padding(20.dp))

    }
}