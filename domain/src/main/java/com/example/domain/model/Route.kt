package com.example.domain.model

sealed class Route(
    val route: String
) {
    object LaunchPage: Route(route = "Launch")
    object AboutUsPage: Route(route = "AboutUs")
    object OnboardingPage: Route(route = "Onboarding")
    object LoginPage: Route(route = "Login")
    object HomePage: Route(route = "Home")
}