package com.example.feature.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.composables.InputField
import com.example.core.composables.LogoCard
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.UserCredential
import com.example.feature.R

@Composable
fun SignUpPage(
    viewModel: SignUpViewModel,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Brown1
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    height = rspDp(150.dp)
                )
                .padding(horizontal = rspDp(30.dp)),
            verticalAlignment = Alignment.Bottom
        ) {
//            LogoCard(
//                imageId = R.drawable.logo_crpd_1,
//                modifier = Modifier
//                    .size(rspDp(90.dp))
//            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Sign up",
                style = TextStyle(
                    color = White,
                    fontFamily = Kare,
                    fontSize = rspSp(50.sp)
                )
            )
        }

        // InputFields
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Beige1,
                    shape = RoundedCornerShape(topStart = rspDp(100.dp))
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(rspDp(50.dp)))

            InputField(
                label = {
                    Text(
                        text = "Username",
                        style = InputFieldUiParam.labelTextStyle()
                    )
                },
                value = viewModel.username,
                onValueChange = {
                    viewModel.onUsernameChanged(it)
                },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
                maxLength = 32,
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
                        color = InputFieldUiParam.borderColor
                    )
            )

            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            InputField(
                label = {
                    Text(
                        text = "FullName",
                        style = InputFieldUiParam.labelTextStyle()
                    )
                },
                value = viewModel.fullName,
                onValueChange = {
                    viewModel.onFullNameChanged(it)
                },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
                maxLength = 32,
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
                        color = InputFieldUiParam.borderColor
                    )
            )

            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            InputField(
                label = {
                    Text(
                        text = "Email",
                        style = InputFieldUiParam.labelTextStyle()
                    )
                },
                value = viewModel.email,
                onValueChange = {
                    viewModel.onEmailChanged(it)
                },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
                maxLength = 32,
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
                        color = InputFieldUiParam.borderColor
                    )
            )

            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            InputField(
                label = {
                    Text(
                        text = "Password",
                        style = InputFieldUiParam.labelTextStyle()
                    )
                },
                value = viewModel.password,
                onValueChange = {
                    viewModel.onPasswordChanged(it)
                },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
                maxLength = 32,
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
                        color = InputFieldUiParam.borderColor
                    ),
                isPasswordField = true
            )

            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            InputField(
                label = {
                    Text(
                        text = "Confirm Password",
                        style = InputFieldUiParam.labelTextStyle()
                    )
                },
                value = viewModel.confirmPassword,
                onValueChange = {
                    viewModel.onConfirmPasswordChanged(it)
                },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
                maxLength = 32,
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
                        color = InputFieldUiParam.borderColor
                    ),
                isPasswordField = true
            )

            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            InputField(
                label = {
                    Text(
                        text = "Province",
                        style = InputFieldUiParam.labelTextStyle()
                    )
                },
                value = viewModel.province,
                onValueChange = {
                    viewModel.onProvinceChanged(it)
                },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
                maxLength = 32,
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
                        color = InputFieldUiParam.borderColor
                    ),
            )

        }

    }

}

object InputFieldUiParam{

    @Composable
    fun clipShape(): RoundedCornerShape
            = RoundedCornerShape(size = rspDp(15.dp))

    val borderColor: Color = Brown1
    val fillColor: Color = White

    val borderWidth: Dp =  2.dp

    const val width: Float = 0.8f
    val height: Dp = 58.dp

    @Composable
    fun inputTextStyle(): TextStyle = TextStyle(
        fontSize = rspSp(15.sp),
        fontFamily = FontFamily.Default
    )

    @Composable
    fun labelTextStyle(): TextStyle = TextStyle(
        fontSize = rspSp(15.sp),
        fontFamily = GlacialIndifferenceBold
    )

}