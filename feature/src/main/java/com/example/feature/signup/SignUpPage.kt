package com.example.feature.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.core.composables.DropdownField
import com.example.core.composables.Footer
import com.example.core.composables.InputField
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Black
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Route
import com.example.domain.model.SignUpCredential
import com.example.domain.model.Terms

@Composable
fun SignUpPage(
    viewModel: SignUpViewModel,
    navController: NavController
) {

    LaunchedEffect(Unit) {
        viewModel.loadTerms()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Brown1
            )
            .statusBarsPadding()
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
                )
                .clip(RoundedCornerShape(topStart = rspDp(100.dp)))
                .verticalScroll(rememberScrollState()),
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
                maxLength = InputFieldUiParam.charLength,
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
                        color = if (viewModel.usernameError) Color.Red else InputFieldUiParam.borderColor
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
                maxLength = InputFieldUiParam.charLength,
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
                        color = if (viewModel.fullNameError) Color.Red else InputFieldUiParam.borderColor
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
                maxLength = InputFieldUiParam.charLength,
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
                maxLength = InputFieldUiParam.charLength,
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
                maxLength = InputFieldUiParam.charLength,
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
                        color = if (viewModel.confirmPasswordError) Color.Red else InputFieldUiParam.borderColor
                    ),
                isPasswordField = true
            )

            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            DropdownField(
                label = {
                    Text(
                        text = "Location",
                        style = InputFieldUiParam.labelTextStyle()
                    )
                },
                value = viewModel.province,
                onValueChange = { name ->
                    val selected = viewModel.getLocations().first{ it.name == name}
                    viewModel.onLocationChanged(selected)
                },
                options = viewModel.getLocations().map { it.name },
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

            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            InputField(
                label = {
                    Text(
                        text = "Farm",
                        style = InputFieldUiParam.labelTextStyle()
                    )
                },
                value = viewModel.farm,
                onValueChange = {
                    viewModel.onFarmChanged(it)
                },
                textStyle = InputFieldUiParam.inputTextStyle(),
                singleLine = true,
                maxLength = InputFieldUiParam.charLength,
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

            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            DropdownField(
                label = {
                    Text(
                        text = "SignUpAs",
                        style = InputFieldUiParam.labelTextStyle()
                    )
                },
                value = viewModel.registeredAs,
                onValueChange = {
                    viewModel.onRegisterAsChanged(it)
                },
                options = viewModel.getUserTag().map { it },
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
            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            val state = viewModel.uiState
            var showDialog by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = rspDp(22.dp)),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = viewModel.isTermsAccepted,
                        onCheckedChange = { checked ->
                            viewModel.isTermsAccepted = checked
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Brown1,
                            checkmarkColor = White,
                            uncheckedColor = Brown1
                        )
                    )
                    Text(
                        text = "I agree to the Terms and privacy policy",
                        style = TextStyle(
                            fontFamily = GlacialIndifference,
                            fontSize = rspSp(14.sp),
                            color = Brown1
                        ),
                        modifier = Modifier
                            .clickable{
                                showDialog = true
                            }
                    )
                }

                if (showDialog) {
                    Dialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                    ) {
                        Surface(
                            tonalElevation = 6.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = rspDp(20.dp)
                                )
                                .clip(
                                    shape = RoundedCornerShape(rspDp(20.dp))
                                ),
                            color = Color.Transparent
                        ) {
                            if (state.isLoading) {
                                Box(Modifier.padding(24.dp)) {
                                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                                }
                            } else if (state.error != null) {
                                Text(
                                    text = state.error,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(24.dp)
                                )
                            } else {
                                TermsContent(
                                    terms = state.terms!!,
                                    onClose = { showDialog = false }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(rspDp(10.dp)))

            Button(
                onClick = {
                    if(viewModel.isTermsAccepted) {
                        val usernamePattern = "^[A-Za-z]+$".toRegex()
                        val fullNamePattern = "^[A-Za-z ]+$".toRegex()
                        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
                        // Enhanced password pattern - alphanumeric only with at least one letter and one number
                        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$".toRegex()

                        viewModel.usernameError = false
                        viewModel.fullNameError = false
                        viewModel.emailError = false
                        viewModel.passwordError = false
                        viewModel.confirmPasswordError = false

                        var firstErrorMessage: String? = null

                        if (viewModel.username.isBlank()) {
                            viewModel.usernameError = true
                            if (firstErrorMessage == null) firstErrorMessage = "Username cannot be empty"
                        } else if (!viewModel.username.matches(usernamePattern)) {
                            viewModel.usernameError = true
                            if (firstErrorMessage == null) firstErrorMessage = "Username must contain only letters"
                        }

                        if (viewModel.fullName.isBlank()) {
                            viewModel.fullNameError = true
                            if (firstErrorMessage == null) firstErrorMessage = "Full name cannot be empty"
                        } else if (!viewModel.fullName.matches(fullNamePattern)) {
                            viewModel.fullNameError = true
                            if (firstErrorMessage == null) firstErrorMessage = "Full name must contain only letters and spaces"
                        }

                        if (viewModel.email.isBlank()) {
                            viewModel.emailError = true
                            if (firstErrorMessage == null) firstErrorMessage = "Email cannot be empty"
                        } else if (!viewModel.email.matches(emailPattern)) {
                            viewModel.emailError = true
                            if (firstErrorMessage == null) firstErrorMessage = "Invalid email address"
                        }

                        if (viewModel.password.isBlank()) {
                            viewModel.passwordError = true
                            if (firstErrorMessage == null) firstErrorMessage = "Password cannot be empty"
                        } else {
                            when {
                                viewModel.password.length < 8 -> {
                                    viewModel.passwordError = true
                                    if (firstErrorMessage == null) firstErrorMessage = "Password must be at least 8 characters long"
                                }
                                !viewModel.password.matches(passwordPattern) -> {
                                    viewModel.passwordError = true
                                    if (firstErrorMessage == null) firstErrorMessage = "Password must contain only letters and numbers, with at least one of each"
                                }
                                viewModel.password.all { it.isDigit() } -> {
                                    viewModel.passwordError = true
                                    if (firstErrorMessage == null) firstErrorMessage = "Password must contain at least one letter"
                                }
                                viewModel.password.all { it.isLetter() } -> {
                                    viewModel.passwordError = true
                                    if (firstErrorMessage == null) firstErrorMessage = "Password must contain at least one number"
                                }
                                viewModel.password.equals(viewModel.username, ignoreCase = true) -> {
                                    viewModel.passwordError = true
                                    if (firstErrorMessage == null) firstErrorMessage = "Password should not be same as username"
                                }
                            }
                        }

                        if (viewModel.confirmPassword.isBlank()) {
                            viewModel.confirmPasswordError = true
                            if (firstErrorMessage == null) firstErrorMessage = "Confirm password cannot be empty"
                        } else if (viewModel.password != viewModel.confirmPassword) {
                            viewModel.confirmPasswordError = true
                            if (firstErrorMessage == null) firstErrorMessage = "Passwords do not match"
                        }

                        if (firstErrorMessage != null) {
                            Toast.makeText(navController.context, firstErrorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.signUp(
                                SignUpCredential(
                                    username = viewModel.username,
                                    fullName = viewModel.fullName,
                                    email = viewModel.email,
                                    password = viewModel.password,
                                    province = viewModel.province,
                                    farm = viewModel.farm,
                                    registeredAs = viewModel.registeredAs
                                )
                            )
                            Log.e("Signup", "Signed Up")
                        }
                    } else {
                        Toast.makeText(navController.context, "Check Our Terms And Conditions First!", Toast.LENGTH_SHORT).show()
                    }
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
                    .fillMaxWidth(fraction = 0.5f)
            ) {
                Text(
                    text = "SIGN UP",
                    style = TextStyle(
                        fontFamily = GlacialIndifferenceBold,
                        fontSize = rspSp(20.sp),
                        color = White
                    )
                )
            }

            Spacer(modifier = Modifier.height(rspDp(20.dp)))

            Row{
                Text(
                    text = "Already Have An Account?",
                    style = TextStyle(
                        color = Brown1,
                        fontFamily = GlacialIndifferenceBold,
                        fontSize = rspSp(15.sp)
                    )
                )
                Text(
                    text = " Sign in",
                    style = TextStyle(
                        color = White,
                        fontFamily = GlacialIndifferenceBold,
                        fontSize = rspSp(15.sp)
                    ),
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                            navController.navigate(Route.LoginPage.route)
                        }
                    )
            }

            Spacer(modifier = Modifier.height(rspDp(40.dp)))

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
                    navController.popBackStack()
                    navController.navigate(Route.AboutUsPage.route)
                },
            )

            Spacer(modifier = Modifier.height(rspDp(20.dp)))

        }
    }
}

@Composable
fun TermsContent(
    terms: Terms,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(
                color = Beige1,
                shape = RoundedCornerShape(rspDp(20.dp))
            )
            .padding(24.dp)
    ) {
        Text(
            text = terms.title,
            style = TextStyle(
                color = Black,
                fontSize = rspSp(25.sp),
                fontFamily = Etna
            )
        )

        Spacer(modifier = Modifier.height(rspDp(20.dp)))

        Text(
            text = terms.intro,
            style = TextStyle(
                color = Black,
                fontSize = rspSp(15.sp),
                fontFamily = GlacialIndifferenceBold
            )
        )

        Spacer(modifier = Modifier.height(rspDp(20.dp)))

        terms.sections.forEach { section ->
            Text(
                text = "${section.number}. ${section.title}",
                style = TextStyle(
                    color = Black,
                    fontSize = rspSp(15.sp),
                    fontFamily = GlacialIndifferenceBold
                ),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = section.content,
                style = TextStyle(
                    color = Black,
                    fontSize = rspSp(13.sp),
                    fontFamily = GlacialIndifference
                )
            )
            Spacer(modifier = Modifier.height(rspDp(20.dp)))
        }

        Spacer(modifier = Modifier.height(rspDp(20.dp)))

        Button(
            onClick = onClose,
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(
                text = "OK",
                style = TextStyle(
                    color = Black,
                    fontSize = rspSp(15.sp),
                    fontFamily = GlacialIndifferenceBold
                )
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