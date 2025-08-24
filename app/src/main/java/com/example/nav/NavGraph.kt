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

package com.example.nav

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.domain.model.Route
import com.example.feature.aboutUs.AboutUsPage
import com.example.feature.community.pages.PostDetailPage
import com.example.feature.community.pages.PostsListPage
import com.example.feature.detection.FeatureSelectionPage
import com.example.feature.detection.DetectionHistoryPage
import com.example.feature.detection.screens.DiagnosisPage
import com.example.feature.detection.screens.PaginatedDetectionPage
import com.example.feature.detection.screens.RealtimeDetectionPage
import com.example.feature.detection.screens.SingleCaptureDetectionPage
import com.example.feature.detection.screens.UploadDetectionPage
import com.example.feature.detection.screens.diagnosis.DiagnosisDetailScreen
import com.example.feature.detection.screens.diagnosis.DiagnosisListScreen
import com.example.feature.geomap.GeoMapPage
import com.example.feature.geomap.GeoMapViewModel
import com.example.feature.home.HomePage
import com.example.feature.launch.LaunchPage
import com.example.feature.launch.LaunchPageViewModel
import com.example.feature.login.LoginPage
import com.example.feature.login.LoginPageViewModel
import com.example.feature.login.forgotPass.ForgotPasswordPage
import com.example.feature.login.forgotPass.PassWordResetViewModel
import com.example.feature.login.forgotPass.ResetPasswordPage
import com.example.feature.login.verify.VerifyUserPage
import com.example.feature.navigation.NavigationBar
import com.example.feature.notification.NotificationPage
import com.example.feature.notification.NotificationScreen
import com.example.feature.onboarding.OnboardingPage
import com.example.feature.onboarding.OnboardingPageViewModel
import com.example.feature.profile.UserProfilePage
import com.example.feature.profile.updateProfile.UpdateProfilePage
import com.example.feature.signup.SignUpPage
import com.example.feature.signup.SignUpViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    activity: Activity
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val navRoutes = listOf(
        Route.HomePage.route,
        Route.DetectionHistoryPage.route,
//        Route.FeatureSelectionPage.route,
        Route.NotificationPage.route,
        Route.UserProfilePage.route
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.LaunchPage.route,
            modifier = Modifier
                .matchParentSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            composable(Route.LaunchPage.route) {
                val viewModel: LaunchPageViewModel = hiltViewModel()
                LaunchPage(viewModel, navController)
            }
            composable(Route.AboutUsPage.route) { AboutUsPage() }
            composable(Route.OnboardingPage.route) {
                val viewModel: OnboardingPageViewModel = hiltViewModel()
                OnboardingPage(viewModel, navController)
            }
            composable(Route.LoginPage.route) {
                val viewModel: LoginPageViewModel = hiltViewModel()
                LoginPage(navController, viewModel, clientId = "", activity = activity)
            }
            composable(Route.HomePage.route) { HomePage(navController) }
            composable(Route.SignUp.route) {
                val viewModel: SignUpViewModel = hiltViewModel()
                SignUpPage(viewModel, navController)
            }
            composable(Route.RealtimeDetectionPage.route) {
                RealtimeDetectionPage(navController = navController)
            }
            composable(Route.SingleImageDetectionPage.route) {
                //SingleImageDetectionPage()
            }
            composable(Route.UserProfilePage.route) {
                UserProfilePage(navController = navController)
            }
            composable(Route.DetectionHistoryPage.route) {
                VerifyUserPage()
            }
            composable(
                route = Route.FeatureSelectionPage.route
            ) {
                RealtimeDetectionPage(navController = navController)
            }
            composable(Route.GeoMapPage.route) {
                val viewModel: GeoMapViewModel = hiltViewModel()
                GeoMapPage(viewModel)
            }
            composable(Route.ForgotPasswordPage.route) {
                val viewModel: PassWordResetViewModel = hiltViewModel()
                ForgotPasswordPage(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(
                route = Route.ResetPasswordPage.route,
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "beanyapp://password-reset"
                    }
                )
            ) { backStackEntry ->
                val deepLinkUri = backStackEntry
                    .arguments
                    ?.getParcelable<Intent>("android-support-nav:controller:deepLinkIntent")
                    ?.data
                ResetPasswordPage(
                    navController = navController,
                    deepLinkUri = deepLinkUri
                    )
            }
            composable(Route.PostsListPage.route) {
                PostsListPage(
                    onPostClick = { postId ->
                        navController.navigate(Route.PostDetailPage.createRoute(postId))
                    }
                )
            }
            composable(Route.PostDetailPage.route) { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId")?.toLongOrNull()

                if (postId != null) {
                    PostDetailPage(
                        postId = postId,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
            composable(Route.UploadDetectionPage.route) {
                UploadDetectionPage(
                    onImagesPicked = { uris ->
                        // Save the URIs to the back stack to pass to next screen
                        navController.currentBackStackEntry?.savedStateHandle?.set("images", uris)
                        navController.navigate(Route.PaginatedDetectionPage.route)
                    }
                )
            }

            composable(Route.PaginatedDetectionPage.route) {
                val uris: List<Uri> =
                    navController.previousBackStackEntry?.savedStateHandle?.get<List<Uri>>("images")
                        ?: emptyList()
                PaginatedDetectionPage(imageUris = uris)
            }

            composable(
                route = Route.UpdateProfilePage.route
            ) {
                UpdateProfilePage(
                    navController = navController
                )
            }
            composable(
                route = Route.DiagnosisPage.route
            ) {
                DiagnosisPage()
            }

            composable(Route.DiagnosisListPage.route) {
                DiagnosisListScreen(
                    navController = navController,
                    onDiagnosisClick = { diagnosisId ->
                        navController.navigate(Route.DiagnosisDetailPage.createRoute(diagnosisId))
                    }
                )
            }

            composable(Route.DiagnosisDetailPage.route) { backStackEntry ->
                val diagnosisId = backStackEntry.arguments?.getString("diagnosisId")?.toLongOrNull()
                if (diagnosisId != null) {
                    DiagnosisDetailScreen(
                        diagnosisId = diagnosisId,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            composable(Route.NotificationPage.route) {
                NotificationPage(navController = navController)
            }

            composable(Route.SingleImageDetectionPage.route) {
                SingleCaptureDetectionPage(navController = navController)
            }

            composable(
                route = "notification_screen/{message}",
                arguments = listOf(navArgument("message") { type = NavType.StringType }),
            ) { backStackEntry ->
                val diagnosisId = backStackEntry.arguments?.getString("message") ?: "No message"
//                NotificationScreen(message)
                DiagnosisDetailScreen(
                    diagnosisId = diagnosisId.toLong(),
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Route.VerifyUserPage.route) {
                VerifyUserPage()
            }

        }

        if (currentRoute in navRoutes) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                NavigationBar(
                    currentRoute = currentRoute,
                    onTabSelected = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(Route.HomePage.route) // { inclusive = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                )
            }
        }
    }
}
