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

package com.example.core.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Brown1
import com.example.core.utils.rspSp

@Composable
fun Footer(
    modifier: Modifier = Modifier,
    color: Color? = Brown1,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
        ) {
            Text(
                text = "About-Us",
                modifier = Modifier
                    .clickable{
                        onClick()
                    },
                style = footerGetTextStyle(color)
            )
            Text(
                text = " | ",
                style = footerGetTextStyle(color)
            )
            Text(
                text = "Beany ",
                style = footerGetTextStyle(color)
            )
            Text(
                text = "© 2025 All Rights Reserved",
                style = footerGetTextStyle(color)
            )
        }
    }
}

@Composable
fun footerGetTextStyle(
    color: Color?
): TextStyle {
    return TextStyle(
        fontSize = rspSp(12.sp),
        color = color!!
    )
}