package com.example.feature.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.Profile

@Composable
fun TestPage(
    viewModel: TestViewModel = hiltViewModel()
) {
    val profiles by viewModel.profiles.collectAsState()

    LazyColumn {
        items(profiles) { profile ->
            ProfileCard(profile = profile)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProfileCard(profile: Profile) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = profile.username ?: "No username", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Full Name: ${profile.fullName ?: "N/A"}")
            Text("Province: ${profile.province ?: "N/A"}")
            Text("Farm: ${profile.farm ?: "N/A"}")
            Text("Registered As: ${profile.registeredAs ?: "N/A"}")
        }
    }
}