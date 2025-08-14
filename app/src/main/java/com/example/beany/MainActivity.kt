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
import com.example.nav.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

val supabase = createSupabaseClient(
    supabaseUrl = "https://moaafjxlduuwjpbgrheo.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1vYWFmanhsZHV1d2pwYmdyaGVvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQzNDczMDMsImV4cCI6MjA2OTkyMzMwM30.RBmHHPgLwGYY0fUllzB6CRMFuL8lltJk72LHfzqYibo"
) {
    install(Auth)
    install(Postgrest)
    install(Realtime)
}

@Serializable
data class Country(val id: Int, val name: String)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(SupabaseExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavGraph(navController = navController, activity = this)

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
//                    val uri = backStackEntry.arguments?.getString("android-support-nav:controller:deepLinkIntent")
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
    val email = "johnpaulvalenzuelaog@gmail.com"

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            scope.launch {
                try {
                    supabase.auth.resetPasswordForEmail(
                        email = email,
                        redirectUrl = "beanyapp://password-reset"
                    )
                    println("Reset link sent to $email")
                } catch (e: Exception) {
                    println("Failed to send reset email: ${e.message}")
                }
            }
        }) {
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

    val accessToken = deepLinkUri?.getQueryParameter("access_token")
    val type = deepLinkUri?.getQueryParameter("type")

    LaunchedEffect(accessToken) {
        if (!accessToken.isNullOrEmpty() && type == "recovery") {
            try {
                supabase.auth.exchangeCodeForSession(accessToken)
                println("Session restored for password reset")
            } catch (e: Exception) {
                println("Failed to restore session: ${e.message}")
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
                if (newPassword == confirmPassword && newPassword.length >= 6) {
                    scope.launch {
                        try {
                            supabase.auth.updateUser {
                                password = newPassword
                            }
                            navController.popBackStack()
                            println("Password updated successfully")
                        } catch (e: Exception) {
                            println("Password update failed: ${e.message}")
                        }
                    }
                } else {
                    println("Passwords do not match or are too short")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Password")
        }
    }
}
