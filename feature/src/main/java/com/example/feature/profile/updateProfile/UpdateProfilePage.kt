package com.example.feature.profile.updateProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.composables.InputField
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.feature.signup.InputFieldUiParam

@Composable
fun UpdateProfilePage(
    viewModel: UpdateProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userId by viewModel.userId.collectAsState()

    Column(

    ) {
        Column(
            modifier = Modifier
                .height(rspDp(150.dp))
                .fillMaxWidth()
                .background(
                    color = Brown1
                )
        ) {

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Beige1
                )
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                value = uiState.profile.fullName ?: "",
                onValueChange = { viewModel.onProfileChange(uiState.profile.copy(fullName = it)) },
                label = { Text("Full Name") },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
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

            InputField(
                value = uiState.profile.username ?: "",
                onValueChange = { viewModel.onProfileChange(uiState.profile.copy(username = it)) },
                label = { Text("Username") },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
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

            InputField(
                value = uiState.profile.province ?: "",
                onValueChange = { viewModel.onProfileChange(uiState.profile.copy(province = it)) },
                label = { Text("Province") },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
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

            InputField(
                value = uiState.profile.farm ?: "",
                onValueChange = { viewModel.onProfileChange(uiState.profile.copy(farm = it)) },
                label = { Text("Farm") },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
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

            Button(
                onClick = {
                    viewModel.onProfileChange(uiState.profile.copy(id = userId))
                    viewModel.updateProfile()
                          },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text(if (uiState.isLoading) "Updating..." else "Update Profile")
            }

            if (uiState.success) {
                Text("Profile updated successfully!", color = MaterialTheme.colorScheme.primary)
            }
            uiState.error?.let {
                Text("Error: $it", color = MaterialTheme.colorScheme.error)
            }
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
    val height: Dp = 63.dp

    val charLength = 64

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