package com.example.beany

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
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
        }
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Beany Channel"
        val descriptionText = "Channel for Beany notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("beany_channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

// 2. Function to show notification

@Composable
fun NotificationButton(context: android.content.Context) {
    // Launcher to request POST_NOTIFICATIONS permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                showNotification(context)
            } else {
                // Handle denial gracefully
                println("Notification permission denied")
            }
        }
    )

    Button(onClick = {
        // Check if permission is already granted
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showNotification(context)
        } else {
            // Request the permission
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }) {
        Text("Show Notification")
    }
}

@androidx.annotation.RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
// Reuse this from before
fun showNotification(context: android.content.Context) {
    val builder = NotificationCompat.Builder(context, "beany_channel_id")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Hello from Beany!")
        .setContentText("This is your notification.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        notify(1001, builder.build())
    }
}