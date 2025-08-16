package com.example.beany

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.core.ui.theme.BeanyTheme
import com.example.nav.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.osmdroid.config.Configuration
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import java.io.File
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

val supabase = createSupabaseClient(
    supabaseUrl = "https://moaafjxlduuwjpbgrheo.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1vYWFmanhsZHV1d2pwYmdyaGVvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQzNDczMDMsImV4cCI6MjA2OTkyMzMwM30.RBmHHPgLwGYY0fUllzB6CRMFuL8lltJk72LHfzqYibo"
) {
    install(Auth)
    install(Postgrest)
    install(Realtime)
    //install other modules
}

@Serializable
data class Country(
    val id: Int,
    val name: String
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(SupabaseExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            BeanyTheme {
                val navHostController = rememberNavController()
                NavGraph(navHostController, activity = this)
            }


//            val navController = rememberNavController()
//
//            NavHost(navController, startDestination = "home") {
//                composable("home") {
//                    HomeScreen(supabase)
//                }
//                composable(
//                    route = "reset",
//                    deepLinks = listOf(
//                        navDeepLink {
//                            uriPattern = "beanyapp://password-reset"
//                        }
//                    )
//                ) { backStackEntry ->
//                    // --- ADDED: Pass the actual deep link URI into the screen ---
//                    val deepLinkUri = intent?.data
//                    ResetPasswordScreen(supabase, navController, deepLinkUri)
//                }
//            }
        }
    }
}

@Composable
fun HomeScreen(supabase: SupabaseClient) {
    val scope = rememberCoroutineScope()
    val email = "johnpaulvalenzuelaog@gmail.com" // <-- use your real test email

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = {
                scope.launch {
                    try {
                        supabase.auth.resetPasswordForEmail(
                            email = email,
                            redirectUrl = "beanyapp://password-reset",

                            )
                        println("Reset link sent to $email")
                    } catch (e: Exception) {
                        println("Failed to send reset email: ${e.message}")
                    }
                }
            }
        ) {
            Text("Send Password Reset Email")
        }
    }
}

@Composable
fun ResetPasswordScreen(
    supabase: SupabaseClient,
    navController: NavController,
    deepLinkUri: Uri?
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var isSessionReady by remember { mutableStateOf(false) }

    val rawUriString = deepLinkUri?.toString() ?: ""
    val fragmentParams = rawUriString
        .substringAfter("#", "")
        .split("&")
        .mapNotNull {
            val parts = it.split("=")
            if (parts.size == 2) parts[0] to parts[1] else null
        }.toMap()

    val accessToken = fragmentParams["access_token"]
    val type = fragmentParams["type"]

    @OptIn(ExperimentalTime::class)
    LaunchedEffect(accessToken) {
        if (!accessToken.isNullOrEmpty() && type == "recovery") {
            try {
                val expiresIn = fragmentParams["expires_in"]?.toLongOrNull() ?: 3600L
                val tokenType = fragmentParams["token_type"] ?: "bearer"

                val session = UserSession(
                    accessToken = accessToken,
                    tokenType = tokenType,
                    refreshToken = fragmentParams["refresh_token"] ?: "",
                    expiresIn = expiresIn,
                    user = null // Supabase can fetch user profile after import
                )

                supabase.auth.importSession(session)
                println("✅ Session manually restored for password reset")
                isSessionReady = true
            } catch (e: Exception) {
                println("❌ Failed to restore session: ${e.message}")
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (!isSessionReady) {
                    println("⚠️ Session not ready yet")
                    return@Button
                }

                if (newPassword != confirmPassword || newPassword.length < 6) {
                    println("⚠️ Passwords do not match or are too short")
                    return@Button
                }

                scope.launch {
                    try {
                        supabase.auth.updateUser {
                            password = newPassword
                        }
                        println("✅ Password updated successfully")
                        navController.popBackStack()
                    } catch (e: Exception) {
                        println("❌ Password update failed: ${e.message}")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Password")
        }
    }
}
