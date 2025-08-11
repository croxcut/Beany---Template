package com.example.feature.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
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
import com.example.core.ui.theme.*
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Route
import com.example.feature.R

data class ProfileItem(
    val icon: Int,
    val title: String,
    val description: String,
    val contentDescription: String
)

@Composable
fun ProfileListItem(
    item: ProfileItem,
    rowPadding: Dp,
    descFontSize: TextUnit,
    titleColor: Color,
    descColor: Color
) {
    Row(
        modifier = Modifier.padding(vertical = rowPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.contentDescription,
            modifier = Modifier
                .size(size = rspDp(30.dp))
                .padding(all = rspDp(4.dp))
        )
        Spacer(modifier = Modifier.width(width = rspDp(2.dp)))
        Column {
            Text(
                text = item.title,
                style = TextStyle(
                    fontFamily = GlacialIndifferenceBold,
                    fontSize = rspSp(15.sp),
                    color = titleColor
                )
            )
            Text(
                text = item.description,
                style = TextStyle(
                    fontFamily = GlacialIndifference,
                    fontSize = descFontSize,
                    color = descColor
                )
            )
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    backgroundColor: Color,
    titleColor: Color,
    items: List<ProfileItem>,
    rowPadding: Dp,
    descFontSize: TextUnit,
    containerPadding: Dp
) {
    Column(
        modifier = Modifier
            .padding(horizontal = containerPadding)
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(size = rspDp(20.dp)))
            .padding(all = rspDp(15.dp))
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = GlacialIndifferenceBold,
                fontSize = rspSp(18.sp),
                color = titleColor
            )
        )
        items.forEachIndexed { index, item ->
            ProfileListItem(
                item = item,
                rowPadding = rowPadding,
                descFontSize = descFontSize,
                titleColor = titleColor,
                descColor = titleColor
            )
            if (index != items.lastIndex) {
                HorizontalDivider(
                    thickness = rspDp(1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = rspDp(35.dp))
                )
            }
        }
    }
}

@Composable
fun ProfilePicture(
    viewModel: UserProfileViewModel,
    onEditClick: () -> Unit
) {
    val isSignedUp by viewModel.isSignedUp.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    LaunchedEffect(isSignedUp) {
        if (isSignedUp) {
            viewModel.downloadProfileImage()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            // Common image modifier with both borders
            val imageModifier = Modifier
                .padding(all = 4.dp)
                .border(width = 4.dp, color = White, shape = CircleShape) // Outer white border
                .padding(4.dp) // Space between borders
                .border(width = 4.dp, color = Brown1, shape = CircleShape) // Inner brown border
                .size(size = 110.dp)
                .clip(CircleShape)

            if (isSignedUp && selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Profile Picture",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.plchldr),
                    contentDescription = "Default Profile Picture",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }

            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile Picture",
                    tint = White
                )
            }
        }
    }
}

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

    val accountItems = listOf(
        ProfileItem(
            icon = R.drawable.profile_tile,
            title = "My Profile",
            description = "Edit Name, Password, Farm, Location",
            contentDescription = "Profile Icon"
        ),
        ProfileItem(
            icon = R.drawable.notif_tile,
            title = "Notification",
            description = "Updates From Beany",
            contentDescription = "Notification Icon"
        ),
        ProfileItem(
            icon = R.drawable.png_tile,
            title = "Uploaded Photos",
            description = "Your Uploaded Photos",
            contentDescription = "Uploaded Photos Icon"
        ),
        ProfileItem(
            icon = R.drawable.table_logo,
            title = "My Diagnosis",
            description = "Check Your Past Diagnosis",
            contentDescription = "Diagnosis Icon"
        )
    )

    val featureItems = listOf(
        ProfileItem(
            icon = R.drawable.camera_tile,
            title = "Camera",
            description = "Scan Cacao Plants with AI!",
            contentDescription = "Camera Icon"
        ),
        ProfileItem(
            icon = R.drawable.scan_history_tile,
            title = "Scan History",
            description = "List of Past Scans",
            contentDescription = "Scan History Icon"
        ),
        ProfileItem(
            icon = R.drawable.community_tile,
            title = "Community",
            description = "Ask questions and learn from other farmers",
            contentDescription = "Community Icon"
        ),
        ProfileItem(
            icon = R.drawable.chat_support_tile,
            title = "Chat Support",
            description = "Chat With Our Registered Experts!",
            contentDescription = "Chat Support Icon"
        )
    )

    val aboutItems = listOf(
        ProfileItem(
            icon = R.drawable.profile_tile,
            title = "About Us",
            description = "Learn about the team behind Beany",
            contentDescription = "About Us Icon"
        ),
        ProfileItem(
            icon = R.drawable.terms_and_condition_tile,
            title = "Terms and Conditions",
            description = "Our guidelines and user agreement",
            contentDescription = "Terms Icon"
        ),
        ProfileItem(
            icon = R.drawable.privacy_tile,
            title = "Privacy Policy",
            description = "How we handle and protect your data",
            contentDescription = "Privacy Icon"
        ),
        ProfileItem(
            icon = R.drawable.contact_tile,
            title = "Contact Us",
            description = "How to reach our support team",
            contentDescription = "Contact Icon"
        )
    )

    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { uri -> viewModel.uploadProfileImage(uri) }
        } else {
            result.error?.printStackTrace()
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            cropImageLauncher.launch(
                CropImageContractOptions(
                    uri,
                    CropImageOptions(
                        guidelines = CropImageView.Guidelines.ON,
                        cropShape = CropImageView.CropShape.RECTANGLE,
                        fixAspectRatio = true
                    )
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Brown1)
            .statusBarsPadding()
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(height = rspDp(10.dp)))
        Text(
            text = "Beany",
            style = TextStyle(fontFamily = Kare, fontSize = rspSp(50.sp), color = White)
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = rspDp(60.dp))
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

                Text(
                    text = profile?.fullName ?: "Guest",
                    style = TextStyle(
                        fontFamily = GlacialIndifferenceBold,
                        color = White,
                        fontSize = rspSp(28.sp)
                    )
                )
                Spacer(modifier = Modifier.height(height = rspDp(10.dp)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = profile?.farm ?: "N/A",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            color = White,
                            fontSize = rspSp(14.sp)
                        )
                    )
                    Text(
                        text = profile?.province ?: "N/A",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            color = White,
                            fontSize = rspSp(14.sp)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(height = rspDp(10.dp)))

                SectionCard(
                    title = "Account",
                    backgroundColor = White,
                    titleColor = Brown1,
                    items = accountItems,
                    rowPadding = ROW_PADDING,
                    descFontSize = DESC_FONT_SIZE,
                    containerPadding = CONTAINER_PADDING
                )
                Spacer(modifier = Modifier.height(height = rspDp(10.dp)))
                SectionCard(
                    title = "Feature",
                    backgroundColor = Beige1,
                    titleColor = Brown1,
                    items = featureItems,
                    rowPadding = ROW_PADDING,
                    descFontSize = DESC_FONT_SIZE,
                    containerPadding = CONTAINER_PADDING
                )
                Spacer(modifier = Modifier.height(height = rspDp(10.dp)))
                SectionCard(
                    title = "About Beany",
                    backgroundColor = White,
                    titleColor = Brown1,
                    items = aboutItems,
                    rowPadding = ROW_PADDING,
                    descFontSize = DESC_FONT_SIZE,
                    containerPadding = CONTAINER_PADDING
                )
                Spacer(modifier = Modifier.height(height = rspDp(15.dp)))

                val buttonText: String = if (isSignedUp) "Log Out" else "Sign In"
                val onButtonClick: () -> Unit = {
                    if (isSignedUp) {
                        viewModel.logout()
                    }
                    navController.navigate(route = Route.LaunchPage.route) {
                        popUpTo(route = Route.LaunchPage.route) {
                            inclusive = true
                        }
                    }
                }
                Button(
                    onClick = onButtonClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .background(
                            color = Color(0xffe8d0a7),
                            shape = RoundedCornerShape(size = rspDp(20.dp))
                        )
                        .height(height = rspDp(30.dp))
                        .width(width = rspDp(200.dp))
                ) {
                    Text(
                        text = buttonText,
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            color = Brown1
                        )
                    )
                }

                Spacer(modifier = Modifier.height(height = rspDp(15.dp)))
                Text(
                    text = "Beany",
                    style = TextStyle(
                        fontFamily = Kare,
                        fontSize = rspSp(20.sp),
                        color = Brown1
                    )
                )

                Footer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = rspDp(100.dp)),
                    onClick = {
                        navController.navigate(route = Route.AboutUsPage.route)
                    }
                )
            }

            // Profile Picture
            ProfilePicture(
                viewModel = viewModel,
                onEditClick = { imagePickerLauncher.launch("image/*") },
            )
        }
    }
}