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

package com.example.feature.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.domain.model.NavBarItem
import com.example.domain.model.Route
import com.example.feature.R

@Composable
fun NavigationBar(
    currentRoute: String?,
    onTabSelected: (String) -> Unit,
    isLoading: Boolean = false // Add this flag
) {
    val items = listOf(
        NavBarItem(Route.HomePage.route, "Home", R.drawable.home_icon),
        NavBarItem(Route.GeoMapPage.route, "History", R.drawable.history_icon),
        NavBarItem(Route.FeatureSelectionPage.route, "Feature", R.drawable.camera_icon),
        NavBarItem(Route.NotificationPage.route, "Notification", R.drawable.notification_icon),
        NavBarItem(Route.UserProfilePage.route, "Profile", R.drawable.profile_icon),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(rspDp(80.dp))
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Bottom bar background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(rspDp(50.dp))
                .background(White)
                .align(Alignment.BottomCenter)
                .pointerInput(Unit) {}
        )

        // Navigation items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Transparent)
                .padding(bottom = rspDp(10.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            items.forEach { item ->
                val isSelected = item.route == currentRoute
                val isFeature = item.route == Route.FeatureSelectionPage.route

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onTabSelected(item.route) }
                        .padding(6.dp)
                ) {
                    Image(
                        painter = painterResource(item.icon),
                        contentDescription = "Icon",
                        modifier = if (isFeature) Modifier
                            .background(Brown1, CircleShape)
                            .border(
                                color = Brown1,
                                width = 3.dp,
                                shape = CircleShape
                            )
                            .size(rspDp(50.dp))
                        else Modifier
                    )

                    Spacer(modifier = Modifier.height(rspDp(4.dp)))

                    HorizontalDivider(
                        thickness = rspDp(2.dp),
                        modifier = if (isFeature) Modifier.width(rspDp(50.dp)) else Modifier.width(rspDp(25.dp)),
                        color = if (isSelected) Brown1 else Color.Transparent
                    )
                }
            }
        }

        // Full-screen loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000)) // semi-transparent overlay
                    .align(Alignment.Center)
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Brown1
                )
            }
        }
    }
}