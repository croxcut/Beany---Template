package com.example.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun rspDp(baseDp: Dp): Dp {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val scaleFactor = screenWidth / 360f
    return (baseDp.value * scaleFactor).dp
}

@Composable
fun rspSp(baseSp: TextUnit): TextUnit {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val scaleFactor = screenWidth / 360f
    return (baseSp.value * scaleFactor).sp
}