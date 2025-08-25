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

package com.example.feature.profile

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.core.composables.Footer
import com.example.core.ui.theme.*
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.Route
import com.example.feature.R
import com.example.feature.signup.TermsContent
import kotlinx.coroutines.coroutineScope

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
    descColor: Color,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .padding(vertical = rowPadding)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
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
    containerPadding: Dp,
    onItemClick: (ProfileItem) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(horizontal = containerPadding)
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(size = rspDp(20.dp)))
            .padding(horizontal = rspDp(40.dp), vertical = rspDp(15.dp))
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
                descColor = titleColor,
                onClick = { onItemClick(item) }
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
    val uploadState by viewModel.uploadState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    LaunchedEffect(isOnline) {
        viewModel.checkConnectivity()
        if (isOnline) {
            coroutineScope {
                viewModel.downloadProfileImage()
            }
            coroutineScope {
                viewModel.refreshSession()
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            if (uploadState is UploadState.Loading) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(Brown1.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = White)
                }
            } else {
                val imageModifier = Modifier
                    .padding(all = 4.dp)
                    .border(width = 4.dp, color = White, shape = CircleShape)
                    .padding(4.dp)
                    .border(width = 4.dp, color = Brown1, shape = CircleShape)
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

                if (isSignedUp) {
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
    }
}

@Composable
fun UserProfilePage(
    viewModel: UserProfileViewModel = hiltViewModel(),
    termsViewModel: TermsViewModel = hiltViewModel(),
    navController: NavController
) {
    val profile by viewModel.profile.collectAsState()
    val isSignedUp by viewModel.isSignedUp.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val context = LocalContext.current

    val isLoading by viewModel.isLoading.collectAsState()

    val termsState by termsViewModel.uiState.collectAsState()

    if (termsState.showDialog) {
        Dialog(
            onDismissRequest = {
                termsViewModel.dismissDialog()
            },
        ) {
            Surface(
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = rspDp(20.dp))
                    .clip(shape = RoundedCornerShape(rspDp(20.dp))),
                color = Color.Transparent
            ) {
                if (termsState.isLoading) {
                    Box(Modifier.padding(24.dp)) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                } else if (termsState.error != null) {
                    Text(
                        text = termsState.error!!,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(24.dp)
                    )
                } else if (termsState.terms != null) {
                    TermsContent(
                        terms = termsState.terms!!,
                        onClose = { termsViewModel.dismissDialog() }
                    )
                }
            }
        }
    }

    val TOP_SIZE_CLIP: Dp = rspDp(120.dp)
    val DESC_FONT_SIZE: TextUnit = rspSp(12.sp)
    val ROW_PADDING: Dp = rspDp(10.dp)
    val CONTAINER_PADDING: Dp = rspDp(15.dp)

    LaunchedEffect(Unit) {
        viewModel.checkConnectivity()
        if (isOnline) {
            viewModel.initializeData()
        }
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    LaunchedEffect(currentDestination) {
        if (currentDestination?.route == Route.UserProfilePage.route) {
            viewModel.checkConnectivity()
            if (isOnline) {
                viewModel.initializeData()
            }
        }
    }

    var showPrivacyDialog by remember { mutableStateOf(false) }
    val privacyPolicyContent = """
        PRIVACY POLICY - BEANY APP
        
        LAST UPDATED: MAY 2025
        
        1. INTRODUCTION
        Welcome to Beany! We respect your privacy and are committed to protecting your personal information. This Privacy Policy explains how we collect, use, disclose, and safeguard your information when you use our agricultural mobile application.
        
        2. INFORMATION WE COLLECT
        
        2.1 Personal Information:
        - Full name, email address, phone number
        - Farm location, province, and geographical coordinates
        - Farm size, crop types, and agricultural practices
        - Profile pictures and farm photographs
        
        2.2 Agricultural Data:
        - Crop health information and growth stages
        - Plant disease detection results and scan data
        - Soil conditions and farming techniques
        - Harvest data and yield information
        - Pest management practices
        
        2.3 Technical Information:
        - Device type, operating system, and unique device identifiers
        - IP address, browser type, and mobile network information
        - App usage statistics and feature interactions
        - Crash reports and performance data
        
        2.4 Location Data:
        - Precise GPS coordinates of your farm
        - Regional agricultural data for personalized recommendations
        - Weather and climate information specific to your location
        
        3. HOW WE USE YOUR DATA
        
        3.1 Service Provision:
        - Provide personalized farming recommendations and alerts
        - Enable plant disease detection and diagnosis
        - Offer customized agricultural advice based on your location
        - Facilitate communication with agricultural experts
        
        3.2 Improvement & Development:
        - Enhance our AI models for better plant disease recognition
        - Develop new features based on user needs and feedback
        - Conduct agricultural research and analysis
        - Improve app performance and user experience
        
        3.3 Communication:
        - Send important agricultural updates and weather alerts
        - Provide farming tips and best practices
        - Notify about new features and updates
        - Offer customer support and technical assistance
        
        3.4 Legal Compliance:
        - Comply with agricultural regulations and standards
        - Fulfill legal obligations and respond to legal requests
        - Protect our rights and prevent fraudulent activities
        
        4. DATA SHARING AND DISCLOSURE
        
        4.1 Service Providers:
        - Cloud storage providers for data hosting
        - Analytics services for app improvement
        - Agricultural experts for consultation services
        - Payment processors (if applicable)
        
        4.2 Aggregated Data:
        - We may share anonymized, aggregated agricultural data with research institutions
        - Statistical information about crop health trends regionally
        - Agricultural insights without personal identification
        
        4.3 Legal Requirements:
        - When required by law or governmental authorities
        - To protect our legal rights and property
        - In emergency situations involving user safety
        
        5. DATA SECURITY
        
        We implement comprehensive security measures including:
        - End-to-end encryption for all sensitive data
        - Regular security audits and vulnerability assessments
        - Secure server infrastructure with firewalls
        - Access controls and authentication protocols
        - Regular data backups and disaster recovery plans
        
        6. DATA RETENTION
        
        We retain your information only as long as necessary for:
        - Providing ongoing services to you
        - Compliance with legal obligations
        - Agricultural research and improvement purposes
        - Resolving disputes and enforcing agreements
        
        7. YOUR RIGHTS
        
        You have the right to:
        - Access and review your personal information
        - Correct inaccurate or incomplete data
        - Request deletion of your personal data
        - Export your data in a portable format
        - Object to certain data processing activities
        - Withdraw consent where consent-based processing
        
        8. CHILDREN'S PRIVACY
        
        Our services are intended for farmers and agricultural professionals aged 18 years and above. We do not knowingly collect information from children under 18.
        
        9. INTERNATIONAL DATA TRANSFERS
        
        Your data may be processed and stored in servers located outside your country of residence. We ensure adequate protection through standard contractual clauses and security measures.
        
        10. CHANGES TO THIS POLICY
        
        We may update this policy periodically. Significant changes will be notified through:
        - In-app notifications and announcements
        - Email communications to registered users
        - Updated version timestamp in the app
        
        11. CONTACT INFORMATION
        
        For privacy-related inquiries, please contact:
        - Email: privacy@beanyapp.com (primary)
        - Support: support@beanyapp.com
        - Agricultural Hotline: +1-800-BEANY-AG
        - Mail: Beany Agricultural Technologies
          123 Farm Innovation Road
          Agricultural District, CA 94102
        
        12. DISPUTE RESOLUTION
        
        Any privacy-related disputes will be resolved through:
        - Initial contact and resolution with our support team
        - Mediation services if required
        - Arbitration in accordance with agricultural industry standards
        
        13. CONSENT
        
        By using Beany application, you acknowledge that:
        - You have read and understood this Privacy Policy
        - You consent to the collection and use of your information as described
        - You agree to receive agricultural communications and updates
        - You understand your rights regarding your personal data
        
        14. AGRICULTURAL DATA SPECIFICS
        
        14.1 Crop Data:
        - We collect data on crop types, varieties, and planting dates
        - Growth stage information and health indicators
        - Yield predictions and harvest quality data
        
        14.2 Soil Information:
        - Soil composition and nutrient levels
        - Irrigation practices and water usage
        - Fertilizer and amendment applications
        
        14.3 Pest Management:
        - Pest identification and infestation levels
        - Treatment methods and effectiveness
        - Integrated pest management strategies
        
        15. THIRD-PARTY INTEGRATIONS
        
        We may integrate with:
        - Weather services for agricultural forecasting
        - Agricultural equipment manufacturers
        - Research institutions for data collaboration
        - Government agricultural departments
        
        16. EMERGENCY SITUATIONS
        
        In agricultural emergency situations (disease outbreaks, natural disasters), we may:
        - Share critical information with agricultural authorities
        - Provide aggregated data for emergency response planning
        - Send emergency alerts to affected farmers
        
        17. DATA ACCURACY
        
        We rely on users to provide accurate agricultural information. We are not responsible for decisions made based on inaccurate user-provided data.
        
        18. FARMER COMMUNITY
        
        When using community features:
        - Your agricultural insights may be shared with other farmers
        - Personal identification is protected in community discussions
        - Best practices and success stories may be featured anonymously
        
        19. COMPLIANCE WITH AGRICULTURAL STANDARDS
        
        We adhere to:
        - Good Agricultural Practices (GAP) guidelines
        - Sustainable farming principles
        - Local agricultural regulations and standards
        
        20. FEEDBACK AND SUGGESTIONS
        
        Any feedback or suggestions you provide becomes our property and may be used to improve our services without compensation.
        
        By continuing to use Beany, you agree to the terms outlined in this comprehensive Privacy Policy.
        """.trimIndent()

    if (showPrivacyDialog) {
        Dialog(
            onDismissRequest = { showPrivacyDialog = false },
        ) {
            Surface(
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = rspDp(20.dp))
                    .clip(shape = RoundedCornerShape(rspDp(20.dp))),
                color = Beige1
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Privacy Policy",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = 20.sp,
                            color = Brown1
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Last Updated: January 2024",
                        style = TextStyle(
                            fontFamily = GlacialIndifference,
                            fontSize = 12.sp,
                            color = Color.Gray
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = privacyPolicyContent,
                            style = TextStyle(
                                fontFamily = GlacialIndifference,
                                fontSize = 14.sp,
                                color = Color.Black,
                                lineHeight = 20.sp
                            )
                        )
                    }

                    Button(
                        onClick = { showPrivacyDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Brown1),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "Close",
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = 16.sp,
                                color = White
                            )
                        )
                    }
                }
            }
        }
    }

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
            title = "Verify User Identity",
            description = "Verify Registered Experds",
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
            result.uriContent?.let { uri ->
                viewModel.uploadProfileImage(uri)
            }
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
                ) { item ->
                    when(item.title) {
                        "My Profile" -> {
                            if(isSignedUp && isOnline) {
                                navController.navigate(Route.UpdateProfilePage.route)
                            } else {
                                Toast.makeText(context, "Please sign or check internet Connection", Toast.LENGTH_SHORT).show()
                            }
                        }
                        "Notification" -> navController.navigate(Route.NotificationPage.route)
//                        "Uploaded Photos" -> navController.navigate(Route.UploadedPhotosPage.route)
                        "My Diagnosis" -> navController.navigate(Route.DiagnosisListPage.route)
                    }
                }

                Spacer(modifier = Modifier.height(height = rspDp(10.dp)))

                SectionCard(
                    title = "Feature",
                    backgroundColor = Beige1,
                    titleColor = Brown1,
                    items = featureItems,
                    rowPadding = ROW_PADDING,
                    descFontSize = DESC_FONT_SIZE,
                    containerPadding = CONTAINER_PADDING
                ) { item ->
                    when(item.title) {
                        "Camera" -> navController.navigate(Route.SingleImageDetectionPage.route)
                        "Scan History" -> navController.navigate(Route.ScanHistoryPage.route)
                        "Community" -> {
                            if (isSignedUp) {
                                navController.navigate(Route.PostsListPage.route)
                            } else {
                                Toast.makeText(context, "Please sign up to access Community", Toast.LENGTH_SHORT).show()
                            }
                        }
                        "Chat Support" -> {
                            if(isSignedUp) {
                                navController.navigate(Route.VerifyUserPage.route)
                            } else {
                                Toast.makeText(context, "Please sign up to access User Verification", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(height = rspDp(10.dp)))

                SectionCard(
                    title = "About Beany",
                    backgroundColor = White,
                    titleColor = Brown1,
                    items = aboutItems,
                    rowPadding = ROW_PADDING,
                    descFontSize = DESC_FONT_SIZE,
                    containerPadding = CONTAINER_PADDING
                ) { item ->
                    when(item.title) {
                        "About Us" -> navController.navigate(Route.AboutUsPage.route)
                        "Terms and Conditions" -> {
                            // Load and show terms when clicked
                            termsViewModel.loadTerms()
                        }
                        "Privacy Policy" -> {
                            showPrivacyDialog = true
                        }
//                        "Contact Us" -> navController.navigate(Route.ContactPage.route)
                    }
                }

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
                        .height(height = rspDp(33.dp))
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
                        color = Beige1
                    )
                )

                Footer(
                    color = Beige1,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = rspDp(100.dp)),
                    onClick = {
                        navController.navigate(route = Route.AboutUsPage.route)
                    }
                )
            }

            ProfilePicture(
                viewModel = viewModel,
                onEditClick = { imagePickerLauncher.launch("image/*") },
            )
        }
    }
}