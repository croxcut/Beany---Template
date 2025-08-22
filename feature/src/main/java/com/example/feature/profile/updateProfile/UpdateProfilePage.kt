package com.example.feature.profile.updateProfile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.core.composables.Footer
import com.example.core.composables.InputField
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.weather.City
import com.example.domain.model.supabase.Profile
import com.example.domain.model.Route
import com.example.feature.signup.InputFieldUiParam

@Composable
fun UpdateProfilePage(
    navController: NavController,
    viewModel: UpdateProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userId by viewModel.userId.collectAsState()

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showFullScreenImage by remember { mutableStateOf(false) }

    val uploadState by viewModel.uploadState.collectAsState()

    val credentialUri by viewModel.credentialUri.collectAsState()

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadCredential(it) }
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            showSuccessDialog = true
        }
    }

    LaunchedEffect(uiState.profile.registeredAs) {
        println("🔥 DEBUG: registeredAs = '${uiState.profile.registeredAs}'")
        println("🔥 DEBUG: Should show credential? ${uiState.profile.registeredAs in listOf("Expert", "Pathologist")}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Brown1
            )
    ) {
        Column(
            modifier = Modifier
                .height(rspDp(150.dp))
                .fillMaxWidth()
        ) {

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Beige1,
                    shape = RoundedCornerShape(topStart = rspDp(100.dp))
                )
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = rspDp(40.dp))
            ) {
                Text(
                    text = "Full Name",
                    style = TextStyle(
                        color = Brown1,
                        fontFamily = Etna,
                        fontSize = rspSp(15.sp)
                    )
                )
                InputField(
                    value = uiState.profile.fullName ?: "",
                    onValueChange = { viewModel.onProfileChange(uiState.profile.copy(fullName = it)) },
                    textStyle = InputFieldUiParam.inputTextStyle(),
                    singleLine = true,
                    modifier = Modifier
                        .height(height = rspDp(50.dp))
                        .fillMaxWidth()
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = rspDp(40.dp))
            ) {
                Text(
                    text = "Username",
                    style = TextStyle(
                        color = Brown1,
                        fontFamily = Etna,
                        fontSize = rspSp(15.sp)
                    )
                )
                InputField(
                    value = uiState.profile.username ?: "",
                    onValueChange = { viewModel.onProfileChange(uiState.profile.copy(username = it)) },
                    textStyle = InputFieldUiParam.inputTextStyle(),
                    singleLine = true,
                    modifier = Modifier
                        .height(height = rspDp(50.dp))
                        .fillMaxWidth()
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

            val cities by viewModel.cities.collectAsState()
            val currentProfile by viewModel.uiState.collectAsState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = rspDp(40.dp))
            ) {
                Text(
                    text = "Province",
                    style = TextStyle(
                        color = Brown1,
                        fontFamily = Etna,
                        fontSize = rspSp(15.sp)
                    )
                )
                CityDropdown(
                    cities = cities,
                    selectedCityName = currentProfile.profile.province,
                    onCitySelected = { selectedCity ->
                        viewModel.onProfileChange(
                            currentProfile.profile.copy(province = selectedCity)
                        )
                    },
                    modifier = Modifier
                        .height(rspDp(50.dp))
                        .fillMaxWidth()
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
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = rspDp(40.dp))
            ) {
                Text(
                    text = "Farm",
                    style = TextStyle(
                        color = Brown1,
                        fontFamily = Etna,
                        fontSize = rspSp(15.sp)
                    )
                )
                InputField(
                    value = uiState.profile.farm ?: "",
                    onValueChange = { viewModel.onProfileChange(uiState.profile.copy(farm = it)) },
                    textStyle = InputFieldUiParam.inputTextStyle(),
                    singleLine = true,
                    modifier = Modifier
                        .height(height = rspDp(50.dp))
                        .fillMaxWidth()
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

            uiState.profile.registeredAs?.let { registeredAs ->
                if (registeredAs in listOf("Expert", "Pathologist")) {
                    Column {
                        Text(
                            text = "CREDENTIAL",
                            style = TextStyle(
                                color = Brown1,
                                fontFamily = Etna,
                                fontSize = 15.sp
                            ),
                            modifier = Modifier.padding(horizontal = 40.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(horizontal = 40.dp)
                                .border(2.dp, Brown1, RoundedCornerShape(20.dp))
                                .clickable {
                                    if (credentialUri != null) {
                                        showFullScreenImage = true
                                    } else if (uiState.profile.verified != true) {
                                        launcher.launch("image/*")
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            when (uploadState) { // Use the collected state here
                                is UploadState.Loading -> {
                                    CircularProgressIndicator(color = Brown1)
                                }
                                else -> {
                                    if (credentialUri != null) {
                                        Image(
                                            painter = rememberAsyncImagePainter(credentialUri),
                                            contentDescription = "Credential",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Fit
                                        )

                                        if (uiState.profile.verified != true) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .padding(8.dp)
                                            ) {
                                                IconButton(
                                                    onClick = { launcher.launch("image/*") },
                                                    modifier = Modifier
                                                        .size(30.dp)
                                                        .background(White, CircleShape)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Edit Credential",
                                                        tint = Brown1
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        Text(
                                            text = if (uiState.profile.verified == true)
                                                "No credential available"
                                            else
                                                "Tap to upload credential",
                                            color = Brown1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.onProfileChange(uiState.profile.copy(id = userId))
                    viewModel.updateProfile()
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = rspDp(40.dp))
                    .background(
                        color = Brown1,
                        shape = RoundedCornerShape(rspDp(50.dp))
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                enabled = !uiState.isLoading
            ) {
                Text(
                    text = if (uiState.isLoading) "Updating..." else "Update Profile",
                    style = TextStyle(
                        color = White,
                        fontFamily = Etna,
                        fontSize = rspSp(15.sp)
                    )
                )
            }

            // Loading Overlay
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = White)
                }
            }

            // Success Dialog
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { showSuccessDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showSuccessDialog = false
                            },
                            modifier = Modifier
                                .background(
                                    color = Brown1,
                                    shape = RoundedCornerShape(rspDp(15.dp))
                                )
                        ) {
                            Text(
                                text = "OK",
                                style = TextStyle(
                                    color = White,
                                    fontFamily = GlacialIndifference,
                                    fontSize = rspSp(15.sp)
                                )
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "Profile Updated",
                            style = TextStyle(
                                color = Brown1,
                                fontFamily = Etna,
                                fontSize = rspSp(20.sp)
                            )
                        )
                            },
                    text = {
                        Text(
                            text = "Your profile has been successfully updated!",
                            style = TextStyle(
                                color = Brown1,
                                fontFamily = GlacialIndifference,
                                fontSize = rspSp(15.sp)
                            )
                        )
                           },
                    modifier = Modifier
                        .background(
                            color = Beige1,
                            shape = RoundedCornerShape(rspDp(20.dp))
                        ),
                    containerColor = Color.Transparent
                )
            }

            if (uiState.error != null) {
                AlertDialog(
                    onDismissRequest = {  },
                    confirmButton = {
                        TextButton(onClick = { }) {
                            Text(
                                text = "OK",
                                style = TextStyle(
                                    color = White,
                                    fontFamily = GlacialIndifference,
                                    fontSize = rspSp(15.sp)
                                )
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "Update Failed",
                            style = TextStyle(
                                color = Brown1,
                                fontFamily = Etna,
                                fontSize = rspSp(20.sp)
                            )
                        )
                            },
                    text = {
                        Text(
                            text = uiState.error ?: "Something went wrong",
                            style = TextStyle(
                                color = Brown1,
                                fontFamily = Etna,
                                fontSize = rspSp(20.sp)
                            )
                        )
                    },
                    modifier = Modifier
                        .background(
                            color = Beige1,
                            shape = RoundedCornerShape(rspDp(20.dp))
                        ),
                    containerColor = Color.Transparent
                )
            }
        }

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

@Composable
fun CredentialSection(
    viewModel: UpdateProfileViewModel,
    profile: Profile,
    onEditClick: () -> Unit,
    onViewClick: () -> Unit
) {
    val uploadState by viewModel.uploadState.collectAsState()
    val credentialUri by viewModel.credentialUri.collectAsState()

    Column {
        Text(
            text = "CREDENTIAL",
            style = TextStyle(
                color = Brown1,
                fontFamily = Etna,
                fontSize = 15.sp
            ),
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 40.dp)
                .border(2.dp, Brown1, RoundedCornerShape(20.dp))
                .clickable {
                    if (credentialUri != null) {
                        onViewClick()
                    } else if (profile.verified != true) {
                        onEditClick()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            when {
                uploadState is UploadState.Loading -> {
                    CircularProgressIndicator(color = Brown1)
                }
                credentialUri != null -> {
                    Image(
                        painter = rememberAsyncImagePainter(credentialUri),
                        contentDescription = "Credential",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    // Show edit button only if not verified
                    if (profile.verified != true) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ) {
                            IconButton(
                                onClick = onEditClick,
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(White, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Credential",
                                    tint = Brown1
                                )
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        text = if (profile.verified == true)
                            "No credential available"
                        else
                            "Tap to upload credential",
                        color = Brown1
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDropdown(
    cities: List<City>,
    selectedCityName: String?,
    onCitySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedCityName ?: "Select a city",
                onValueChange = {},
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontFamily = GlacialIndifferenceBold,
                    color = Brown1,
                    fontSize = rspSp(16.sp)
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = Brown1,
                border = BorderStroke(
                    width = rspDp(2.dp),
                    color = Beige1
                ),
                shape = RoundedCornerShape(rspDp(10.dp))
            ) {
                cities.forEach { city ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = city.name,
                                style = TextStyle(
                                    fontSize = rspSp(12.sp),
                                    fontFamily = Etna,
                                    color = White
                                )
                            )
                        },
                        onClick = {
                            onCitySelected(city.name)
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            trailingIconColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}