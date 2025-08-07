package com.example.nav

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.domain.model.Route
import com.example.feature.aboutUs.AboutUsPage
import com.example.feature.detection.FeatureSelectionPage
import com.example.feature.detection.history.DetectionHistoryPage
import com.example.feature.detection.realtime.RealtimeDetectionPage
import com.example.feature.detection.singleImage.SingleImageDetectionPage
import com.example.feature.detection.upload.UploadDetectionPage
import com.example.feature.home.HomePage
import com.example.feature.launch.LaunchPage
import com.example.feature.launch.LaunchPageViewModel
import com.example.feature.login.LoginPage
import com.example.feature.login.LoginPageViewModel
import com.example.feature.navigation.NavigationBar
import com.example.feature.notification.NotificationPage
import com.example.feature.onboarding.OnboardingPage
import com.example.feature.onboarding.OnboardingPageViewModel
import com.example.feature.profile.UserProfilePage
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
        Route.FeatureSelectionPage.route,
        Route.NotificationPage.route,
        Route.UserProfilePage.route
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .navigationBarsPadding()
            .background(Color.Transparent)
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.SignUp.route,
            modifier = Modifier
                .matchParentSize()
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
                SignUpPage(viewModel)
            }
            composable(Route.RealtimeDetectionPage.route) { RealtimeDetectionPage() }
            composable(Route.SingleImageDetectionPage.route) { SingleImageDetectionPage() }
            composable(Route.UploadDetectionPage.route) { UploadDetectionPage() }
            composable(Route.UserProfilePage.route) { UserProfilePage() }
            composable(Route.DetectionHistoryPage.route) { DetectionHistoryPage() }
            composable(Route.FeatureSelectionPage.route) { FeatureSelectionPage() }
            composable(Route.NotificationPage.route) { NotificationPage() }
        }

        if (currentRoute in navRoutes) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                NavigationBar(
                    currentRoute = currentRoute,
                    onTabSelected = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(Route.HomePage.route) { saveState = true }
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

