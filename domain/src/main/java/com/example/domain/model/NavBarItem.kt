package com.example.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class NavBarItem(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
)