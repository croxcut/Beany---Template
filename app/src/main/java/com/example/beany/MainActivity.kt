package com.example.beany

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.core.ui.theme.BeanyTheme
import com.example.feature.aboutUs.AboutUsPage
import com.example.feature.launch.LaunchPage
import com.example.nav.NavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeanyTheme {
                val navHostController = rememberNavController()
                NavGraph(navHostController)
            }
        }
    }
}
