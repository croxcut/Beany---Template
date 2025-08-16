package com.example.domain.model

sealed class Route(
    val route: String
) {
    object LaunchPage: Route(route = "Launch")
    object AboutUsPage: Route(route = "AboutUs")
    object OnboardingPage: Route(route = "Onboarding")
    object LoginPage: Route(route = "Login")
    object HomePage: Route(route = "Home")
    object SignUp: Route(route = "SignUp")
    object UserProfilePage: Route(route = "Profile")

    object NotificationPage: Route(route = "Notification")

    // AI Feature
    object RealtimeDetectionPage: Route(route = "Realtime")
    object SingleImageDetectionPage: Route(route = "SingleImage")
    object UploadDetectionPage: Route(route = "UploadImage")
    object DetectionHistoryPage: Route(route = "History")
    object FeatureSelectionPage: Route(route = "FeatureSelection")

    object ForgotPasswordPage: Route(route = "ForgotPass")
    object ResetPasswordPage: Route(route = "ResetPass")
    object GeoMapPage: Route(route = "GeoMap")

    object PostsListPage: Route(route = "PostsList")
    object PostDetailPage: Route(route = "PostDetail/{postId}") {
        fun createRoute(postId: Long) = "PostDetail/$postId"
    }

    object PaginatedDetectionPage: Route(route = "PaginatedDetection")

    object UpdateProfilePage: Route(route = "UpdateProfile")

    companion object {
        val navRoutes: List<String> by lazy {
            Route::class.sealedSubclasses.mapNotNull { it.objectInstance?.route }
        }
    }
}
