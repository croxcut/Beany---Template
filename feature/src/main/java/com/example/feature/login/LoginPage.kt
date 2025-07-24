package com.example.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
import com.example.domain.model.Route

object InputFieldUiParam{

    @Composable
    fun clipShape(): RoundedCornerShape
            = RoundedCornerShape(size = rspDp(15.dp))

    val borderColor: Color = Brown1
    val fillColor: Color = White

    val borderWidth: Dp =  2.dp

    const val width: Float = 0.8f
    val height: Dp = 80.dp

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

@Composable
fun LoginPage(
    navController: NavController,
    viewModel: LoginPageViewModel
)  {

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
                .fillMaxHeight(0.2f)
                .fillMaxWidth()
//                .background(
//                    color = White
//                )
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
                        text = "Username",
                        style = InputFieldUiParam.labelTextStyle()
                    )
                },
                value = viewModel.userName,
                onValueChange = {
                    viewModel.setUsernameOnChange(it)
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

            Spacer(modifier = Modifier.height(rspDp(15.dp)))

            Button(
                onClick = {
                    val validated = viewModel.validateUser()
                    if (validated) navController.navigate(Route.HomePage.route)
                },
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
                        fontSize = rspSp(20.sp)
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Footer(
                onClick = {
                    navController.navigate(Route.AboutUsPage.route)
                },
                modifier = Modifier
                    .navigationBarsPadding()
            )

        }


    }

}

