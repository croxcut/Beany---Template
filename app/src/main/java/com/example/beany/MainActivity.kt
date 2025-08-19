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
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @OptIn(SupabaseExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        createNotificationChannel(this)
        EasyNotification.createNotificationChannel(
            context = this,
            channelId = "beany_channel",
            channelName = "Beany Notifications"
        )

        setContent {
            BeanyTheme {
                navController = rememberNavController()
                NavGraph(navController = navController, activity = this)
            }
        }

        // Handle notification deep link immediately
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
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

//val navController = rememberNavController()
//
//// Navigate if opened via notification
//LaunchedEffect(Unit) {
//    val screen = intent.getStringExtra("openScreen")
//    val message = intent.getStringExtra("notificationMessage")
//    if (screen != null) {
//        navController.navigate("$screen/$message") {
//            popUpTo(navController.graph.startDestinationId) { inclusive = true }
//        }
//    }
//}
//
//NavHost(navController = navController, startDestination = "home") {
//    composable("home") { HomeScreen(navController) }
//
//    // Receive the message via nav arguments
//    composable(
//        "notification_screen/{message}",
//        arguments = listOf(navArgument("message") { type = NavType.StringType })
//    ) { backStackEntry ->
//        val msg = backStackEntry.arguments?.getString("message") ?: "No message"
//        NotificationScreen(message = msg)
//    }
//}

//
//object EasyNotification {
//
//    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
//    fun show(
//        context: Context,
//        title: String,
//        message: String,
//        channelId: String = "default_channel",
//        channelName: String = "Default",
//        smallIcon: Int = R.drawable.ic_dialog_info,
//        intent: Intent? = null
//    ) {
//        createNotificationChannel(context, channelId, channelName)
//
//        val pendingIntent = intent?.let {
//            PendingIntent.getActivity(
//                context,
//                0,
//                it,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//        }
//
//        val notification = NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(smallIcon)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .apply {
//                pendingIntent?.let { setContentIntent(it) }
//                setAutoCancel(true)
//            }
//            .build()
//
//        NotificationManagerCompat.from(context).notify((System.currentTimeMillis() % 10000).toInt(), notification)
//    }
//
//    private fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                channelName,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
//        }
//    }
//}
//
//@Composable
//fun HomeScreen(navController: NavController) {
//    val context = LocalContext.current
//    val calendar = remember { Calendar.getInstance() }
//
//    var selectedDate by remember { mutableStateOf("") }
//    var selectedTime by remember { mutableStateOf("") }
//    var repeatDaily by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Home Screen")
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            val datePicker = DatePickerDialog(
//                context,
//                { _, year, month, dayOfMonth ->
//                    calendar.set(Calendar.YEAR, year)
//                    calendar.set(Calendar.MONTH, month)
//                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//                    selectedDate = "$dayOfMonth/${month + 1}/$year"
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            )
//            datePicker.show()
//        }) {
//            Text(if (selectedDate.isEmpty()) "Pick Date" else "Date: $selectedDate")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            val timePicker = TimePickerDialog(
//                context,
//                { _, hourOfDay, minute ->
//                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
//                    calendar.set(Calendar.MINUTE, minute)
//                    calendar.set(Calendar.SECOND, 0)
//
//                    // Convert 24-hour to 12-hour format with AM/PM
//                    val amPm = if (hourOfDay >= 12) "PM" else "AM"
//                    val hour12 = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
//                    selectedTime = "%02d:%02d %s".format(hour12, minute, amPm)
//
//                },
//                calendar.get(Calendar.HOUR_OF_DAY),
//                calendar.get(Calendar.MINUTE),
//                false // <-- false means 12-hour format
//            )
//            timePicker.show()
//        }) {
//            Text(if (selectedTime.isEmpty()) "Pick Time" else "Time: $selectedTime")
//        }
//
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Checkbox(
//                checked = repeatDaily,
//                onCheckedChange = { repeatDaily = it }
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text("Repeat Daily")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            scheduleNotification(
//                context = context,
//                calendar = calendar,
//                title = "Hello!",
//                message = "Scheduled Notification",
//                repeatDaily = repeatDaily
//            )
//        }) {
//            Text("Schedule Notification")
//        }
//    }
//}
//
//@Composable
//fun NotificationScreen(message: String) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text("Notification Screen: $message", fontSize = 24.sp)
//    }
//}
//
//class NotificationWorker(
//    context: Context,
//    workerParams: WorkerParameters
//) : Worker(context, workerParams) {
//
//    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
//    override fun doWork(): Result {
//        val title = inputData.getString("title") ?: "Title"
//        val message = inputData.getString("message") ?: "Message"
//        val channelId = inputData.getString("channelId") ?: "default_channel"
//        val channelName = inputData.getString("channelName") ?: "Default"
//
//        val intent = Intent(applicationContext, MainActivity::class.java).apply {
//            putExtra("openScreen", "notification_screen")
//            putExtra("notificationMessage", message)
//        }
//
//        EasyNotification.show(
//            context = applicationContext,
//            title = title,
//            message = message,
//            channelId = channelId,
//            channelName = channelName,
//            intent = intent
//        )
//
//        return Result.success()
//    }
//}
//
//fun scheduleNotification(
//    context: Context,
//    calendar: Calendar,
//    title: String,
//    message: String,
//    repeatDaily: Boolean
//) {
//    val delay = calendar.timeInMillis - System.currentTimeMillis()
//    if (delay <= 0) {
//        Toast.makeText(context, "Selected time is in the past!", Toast.LENGTH_SHORT).show()
//        return
//    }
//
//    val data = workDataOf(
//        "title" to title,
//        "message" to message,
//        "channelId" to "default_channel",
//        "channelName" to "Default"
//    )
//
//    val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
//        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
//        .setInputData(data)
//        .build()
//
//    WorkManager.getInstance(context).enqueue(notificationWork)
//
//    if (repeatDaily) {
//        // Schedule repeating notifications every 24 hours
//        val repeatingWork = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
//            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
//            .setInputData(data)
//            .build()
//
//        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//            "daily_notification",
//            ExistingPeriodicWorkPolicy.REPLACE,
//            repeatingWork
//        )
//    }
//
//    Toast.makeText(context, "Notification Scheduled!", Toast.LENGTH_SHORT).show()
//}