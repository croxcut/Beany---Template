// ===============================================================================
//
// Copyright (C) 2025-2026 by John Paul Valenzuela
//
// This source is available for distribution and/or modification
// only under the terms of the Beany Source Code License as
// published by Beany. All rights reserved.
//
// The source is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// FITNESS FOR A PARTICULAR PURPOSE. See the Beany Source Code License
// for more details.
//
// ===============================================================================

package com.example.feature.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.core.composables.Footer
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
import com.example.domain.model.supabase.LoginCredential
import com.example.domain.model.Route
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.ktor.util.collections.getValue


object InputFieldUiParam {
    @Composable
    fun clipShape(): RoundedCornerShape = RoundedCornerShape(size = rspDp(15.dp))

    val borderColor: Color = Brown1
    val fillColor: Color = White
    val borderWidth: Dp = 2.dp
    const val width: Float = 0.8f
    val height: Dp = 80.dp

    @Composable
    fun inputTextStyle(): TextStyle = TextStyle(
        fontSize = rspSp(15.sp),
        fontFamily = FontFamily.Default,
        color = Brown1
    )

    @Composable
    fun labelTextStyle(): TextStyle = TextStyle(
        fontSize = rspSp(15.sp),
        fontFamily = GlacialIndifferenceBold,
        color = Brown1
    )
}

@Composable
fun LoginPage(
    navController: NavController,
    viewModel: LoginPageViewModel,
    activity: Activity,
    clientId: String
) {
    val success by viewModel.success.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(success) {
        if (success || isLoggedIn) {
            navController.navigate(Route.HomePage.route) {
                popUpTo(Route.LoginPage.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Brown1)
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth()
                    .padding(rspDp(30.dp)),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "beany",
                    style = TextStyle(
                        fontFamily = Kare,
                        color = White,
                        fontSize = rspSp(52.sp)
                    )
                )
                Text(
                    text = "Discover The World of Precision Farming!",
                    style = TextStyle(
                        color = Beige1,
                        fontSize = rspSp(15.sp)
                    ),
                    modifier = Modifier
                        .offset(y = rspDp(-10.dp))
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .background(
                        color = Beige1,
                        shape = RoundedCornerShape(topStart = rspDp(100.dp))
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(rspDp(100.dp)))

                InputField(
                    label = {
                        Text(
                            text = "Email",
                            style = InputFieldUiParam.labelTextStyle()
                        )
                    },
                    value = viewModel.email,
                    onValueChange = {
                        viewModel.setUsernameOnChange(it)
                        viewModel.emailError = false
                    },
                    textStyle = InputFieldUiParam.inputTextStyle(),
                    singleLine = true,
                    maxLength = 64,
                    maxLines = 1,
                    modifier = Modifier
                        .height(height = InputFieldUiParam.height)
                        .fillMaxWidth(fraction = InputFieldUiParam.width)
                        .background(
                            color = InputFieldUiParam.fillColor,
                            shape = InputFieldUiParam.clipShape()
                        )
                        .border(
                            width = InputFieldUiParam.borderWidth,
                            shape = InputFieldUiParam.clipShape(),
                            color = if (viewModel.emailError) Color.Red else InputFieldUiParam.borderColor
                        )
                )

                Spacer(modifier = Modifier.height(rspDp(15.dp)))

                InputField(
                    label = {
                        Text(
                            text = "Password",
                            style = InputFieldUiParam.labelTextStyle()
                        )
                    },
                    value = viewModel.password,
                    onValueChange = {
                        viewModel.setPasswordOnChange(it)
                        viewModel.passwordError = false
                    },
                    textStyle = InputFieldUiParam.inputTextStyle(),
                    singleLine = true,
                    maxLength = 64,
                    maxLines = 1,
                    modifier = Modifier
                        .height(height = InputFieldUiParam.height)
                        .fillMaxWidth(fraction = InputFieldUiParam.width)
                        .background(
                            color = InputFieldUiParam.fillColor,
                            shape = InputFieldUiParam.clipShape()
                        )
                        .border(
                            width = InputFieldUiParam.borderWidth,
                            shape = InputFieldUiParam.clipShape(),
                            color = if (viewModel.passwordError) Color.Red else InputFieldUiParam.borderColor
                        ),
                    isPasswordField = true
                )

                Spacer(modifier = Modifier.height(rspDp(10.dp)))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = rspDp(40.dp))
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Forgot Password?",
                        style = TextStyle(
                            fontFamily = GlacialIndifference,
                            fontStyle = FontStyle.Italic,
                            color = Brown1
                        ),
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Route.ForgotPasswordPage.route)
                            }
                    )
                }

                Spacer(modifier = Modifier.height(rspDp(10.dp)))

                Button(
                    onClick = {
                        var valid = true

                        if (viewModel.email.isBlank()) {
                            viewModel.emailError = true
                            Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                            valid = false
                        }
                        if (viewModel.password.isBlank()) {
                            viewModel.passwordError = true
                            Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                            valid = false
                        }

                        if (valid) {
                            viewModel.login(
                                LoginCredential(
                                    email = viewModel.email,
                                    password = viewModel.password
                                )
                            )
                        }
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier
                        .background(
                            color = Brown1,
                            shape = RoundedCornerShape(rspDp(20.dp))
                        )
                        .fillMaxWidth(fraction = 0.4f)
                ) {
                    Text(
                        text = "SIGN IN",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(20.sp),
                            color = White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Text(
                        text = "Don't have an account?",
                        style = TextStyle(
                            color = Brown1,
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(15.sp)
                        ),
                    )
                    Text(
                        text = " Sign Up",
                        style = TextStyle(
                            color = White,
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(15.sp)
                        ),
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Route.SignUp.route)
                            }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .padding(vertical = rspDp(10.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        color = Brown1,
                        thickness = rspDp(1.dp),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "or",
                        style = TextStyle(
                            fontFamily = GlacialIndifference,
                            fontSize = rspSp(20.sp),
                            fontStyle = FontStyle.Italic,
                            color = Brown1
                        ),
                        modifier = Modifier
                            .padding(horizontal = rspDp(15.dp))
                    )
                    HorizontalDivider(
                        color = Brown1,
                        thickness = rspDp(1.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row {
                    Text(
                        text = "Continue With Google",
                        style = TextStyle(
                            fontFamily = GlacialIndifference,
                            fontSize = rspSp(15.sp),
                            color = Brown1
                        ),
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = rspDp(10.dp)))

                Row {
                    Text(
                        text = "Continue without an account",
                        style = TextStyle(
                            fontFamily = GlacialIndifference,
                            fontSize = rspSp(15.sp),
                            color = Brown1,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Route.HomePage.route)
                            }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Beany",
                    style = TextStyle(
                        color = Brown1,
                        fontFamily = Kare,
                        fontSize = rspSp(20.sp)
                    )
                )

                Footer(
                    onClick = {
                        navController.navigate(Route.AboutUsPage.route)
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(vertical = rspDp(10.dp))
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp),
                color = Brown1,
                strokeWidth = 4.dp
            )
        }
    }
}

// keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

