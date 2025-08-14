package com.example.nav

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navDeepLink
import com.example.beany.PostDetailScreen
import com.example.beany.PostsListScreen
import com.example.core.ui.theme.White
import com.example.domain.model.Route
import com.example.feature.aboutUs.AboutUsPage
import com.example.feature.detection.FeatureSelectionPage
import com.example.feature.detection.history.DetectionHistoryPage
import com.example.feature.detection.realtime.RealtimeDetectionPage
import com.example.feature.detection.upload.UploadDetectionPage
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
import com.example.feature.navigation.NavigationBar
import com.example.feature.notification.NotificationPage
import com.example.feature.onboarding.OnboardingPage
import com.example.feature.onboarding.OnboardingPageViewModel
import com.example.feature.profile.UserProfilePage
import com.example.feature.signup.SignUpPage
import com.example.feature.signup.SignUpViewModel
import com.example.feature.test.UploadPage

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
            composable(Route.RealtimeDetectionPage.route) { RealtimeDetectionPage() }
            composable(Route.SingleImageDetectionPage.route) {
                //SingleImageDetectionPage()
                UploadPage()
            }
            composable(Route.UploadDetectionPage.route) { UploadDetectionPage() }
            composable(Route.UserProfilePage.route) {
                UserProfilePage(navController = navController)
            }
            composable(Route.DetectionHistoryPage.route) {
                DetectionHistoryPage()
//                val viewModel: GeoMapViewModel = hiltViewModel()
//                GeoMapPage(
//                    viewModel = viewModel
//                )
            }
            composable(Route.FeatureSelectionPage.route) { FeatureSelectionPage() }
            composable(Route.NotificationPage.route) { NotificationPage() }
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
                ResetPasswordPage()
            }
            composable(Route.PostsListPage.route) {
                PostsListScreen(
                    onPostClick = { postId ->
                        navController.navigate(Route.PostDetailPage.createRoute(postId))
                    }
                )
            }

            composable(
                route = Route.PostDetailPage.route
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId")?.toLongOrNull()
                postId?.let {
                    PostDetailScreen(postId = it)
                }
            }

        }

        if (currentRoute in navRoutes) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars) // Changed here
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
