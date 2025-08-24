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
