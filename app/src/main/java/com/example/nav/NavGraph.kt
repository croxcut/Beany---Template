package com.example.nav

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.domain.model.Route
import com.example.feature.aboutUs.AboutUsPage
import com.example.feature.home.HomePage
import com.example.feature.launch.LaunchPage
import com.example.feature.launch.LaunchPageViewModel
import com.example.feature.login.LoginPage
import com.example.feature.login.LoginPageViewModel
import com.example.feature.onboarding.OnboardingPage
import com.example.feature.onboarding.OnboardingPageViewModel
import com.example.feature.signup.SignUpPage
import com.example.feature.signup.SignUpViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    activity: Activity
) {

    NavHost(
        navController = navController,
        startDestination = Route.HomePage.route
    ) {
        composable(
            route = Route.LaunchPage.route
        ) {
            val viewModel: LaunchPageViewModel = hiltViewModel()
            LaunchPage(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            route = Route.AboutUsPage.route
        ) {
            AboutUsPage()
        }

        composable(
            route = Route.OnboardingPage.route
        ) {
            val viewModel: OnboardingPageViewModel = hiltViewModel()
            OnboardingPage(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            route = Route.LoginPage.route
        ) {
            val viewModel: LoginPageViewModel = hiltViewModel()
            LoginPage(
                navController = navController,
                viewModel = viewModel,
                clientId = "",
                activity = activity
            )
        }

        composable(
            route = Route.HomePage.route
        ) {
            HomePage()
        }

        composable(
            route = Route.SignUp.route
        ) {
            val viewModel: SignUpViewModel = hiltViewModel()
            SignUpPage(
                viewModel = viewModel
            )
        }

    }

}