package com.example.feature.home

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