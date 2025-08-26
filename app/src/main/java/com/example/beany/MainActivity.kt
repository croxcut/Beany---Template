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

package com.example.beany

import android.Manifest
import android.R
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.core.local.EasyNotification
import com.example.core.local.createNotificationChannel
import com.example.core.ui.theme.BeanyTheme
import com.example.feature.test.ChatScreen
import com.example.nav.NavGraph
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.osmdroid.config.Configuration
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import java.io.File
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requiredPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val storagePermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { (permission, isGranted) ->
                if (isGranted) {
                    Log.d("Permissions", "$permission granted")
                } else {
                    Log.d("Permissions", "$permission denied")
                }
            }
        }

    @RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
    @OptIn(SupabaseExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        createNotificationChannel(this)
        EasyNotification.createNotificationChannel(
            context = this,
            channelId = "beany_channel",
            channelName = "Beany Notifications"
        )
        setContent {
            BeanyTheme {
                navController = rememberNavController()

//                var locationText by remember { mutableStateOf("Press button to get location") }
//
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text(locationText)
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Button(onClick = { getCurrentLocation { lat, lon ->
//                        locationText = "Lat: $lat, Lon: $lon"
//                    } }) {
//                        Text("Get Location")
//                    }
//                }

//                NavGraph(navController = navController, activity = this)
                ChatScreen()
            }
        }

        requestNecessaryPermissions()
        handleIntent(intent)
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getCurrentLocation(onLocationResult: (Double, Double) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Location", "Permission not granted")
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                onLocationResult(it.latitude, it.longitude)
            } ?: run {
                Log.d("Location", "Location is null")
            }
        }
    }

    private fun requestNecessaryPermissions() {
        val allPermissions = requiredPermissions + storagePermissions

        val permissionsToRequest = allPermissions.filter { permission ->
            ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    private fun handleIntent(intent: Intent?) {
        intent?.data?.let { uri ->
            Log.d("EasyNotification", "Notification tapped! URI = $uri")
            val message = uri.lastPathSegment?.let { Uri.decode(it) } ?: return
            navController.navigate("notification_screen/$message") {
                launchSingleTop = true
            }
        }
    }
}

