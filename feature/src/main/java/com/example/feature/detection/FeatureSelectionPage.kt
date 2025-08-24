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

package com.example.feature.detection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Kare
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp

@Composable
fun FeatureSelectionPage(

) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Brown1
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    rspDp(20.dp)
                )
//                .background(
//                    color = Beige1
//                )
        ) {
            Text(
                text = "Beany",
                style = TextStyle(
                    fontFamily = Kare,
                    fontSize = rspSp(40.sp),
                    color = Beige1
                )
            )
        }

        Column(
            modifier = Modifier
                .height(rspDp(80.dp))
                .fillMaxWidth()
                .padding(
                    horizontal = rspDp(15.dp)
                )
                .background(
                    color = Beige1,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {

        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .height(rspDp(80.dp))
                .fillMaxWidth()
                .padding(
                    horizontal = rspDp(15.dp)
                )
                .background(
                    color = Beige1,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {

        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .height(rspDp(80.dp))
                .fillMaxWidth()
                .padding(
                    horizontal = rspDp(15.dp)
                )
                .background(
                    color = Beige1,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {

        }

    }

}