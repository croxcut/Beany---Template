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

package com.example.feature.home.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp


@Composable
fun Modifier.optionsContainerConfig(): Modifier {
    return this
        .height(
            height = rspDp(
                baseDp = 90.dp
            )
        )
        .padding(
            horizontal = rspDp(
                baseDp = 10.dp
            )
        )
        .background(
            color = White,
            shape = RoundedCornerShape(
                size = rspDp(
                    baseDp = 10.dp
                )
            )
        )
}