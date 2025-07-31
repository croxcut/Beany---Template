package com.example.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun rspDp(baseDp: Dp): Dp {
    val config = LocalConfiguration.current
    val scale = remember(config) {
        val screenWidth = config.screenWidthDp
        val screenHeight = config.screenHeightDp
        minOf(screenWidth, screenHeight) / 360f
    }
    return (baseDp.value * scale).dp
}

@Composable
fun rspSp(baseSp: TextUnit): TextUnit {
    val config = LocalConfiguration.current
    val scale = remember(config) {
        val screenWidth = config.screenWidthDp
        val screenHeight = config.screenHeightDp
        minOf(screenWidth, screenHeight) / 360f
    }
    return (baseSp.value * scale).sp
}
