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

package com.example.feature.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.utils.rspSp
import com.example.data.model.ActivityEntity
import java.text.SimpleDateFormat

@Composable
fun ActivityItem(activity: ActivityEntity, formatter: SimpleDateFormat) {
    Column {
        Row {
            Text(
                text = "Activity:",
                style = TextStyle(
                    fontSize = rspSp(15.sp),
                    color = Brown1,
                    fontFamily = GlacialIndifferenceBold
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Date",
                style = TextStyle(
                    fontSize = rspSp(15.sp),
                    color = Brown1,
                    fontFamily = GlacialIndifferenceBold
                )
            )
        }

        Row {
            Text(
                text = activity.activity,
                style = TextStyle(
                    fontSize = rspSp(15.sp),
                    color = Brown1,
                    fontFamily = GlacialIndifferenceBold
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = formatter.format(activity.date),
                style = TextStyle(
                    fontSize = rspSp(15.sp),
                    color = Brown1,
                    fontFamily = GlacialIndifferenceBold
                )
            )
        }
    }
}