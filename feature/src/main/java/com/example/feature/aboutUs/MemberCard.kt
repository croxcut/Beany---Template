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

package com.example.feature.aboutUs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.domain.model.Member


@Composable
fun MemberCard(
    modifier: Modifier = Modifier,
    member: Member
) {

    Box(
        modifier = modifier
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(top = 100.dp)
                .height(200.dp)
                .border(
                    width = 4.dp,
                    color = Beige1,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = member.position,
                color = Brown1,
                fontFamily = Etna,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
            )

            Text(
                text = member.name,
                color = Color.Black,
                fontFamily = GlacialIndifferenceBold,
                fontSize = 15.sp,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = member.bio,
                    color = Color.Black,
                    fontFamily = GlacialIndifference,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
            }

        }

        Image(
            painter = painterResource(member.imageRes),
            contentDescription = "Member-${member.name}",
            modifier = Modifier
                .offset(y = -80.dp)
                .size(150.dp)
                .clip(
                    shape = CircleShape
                )
                .border(
                    width = 4.dp,
                    color = Beige1,
                    shape = CircleShape
                )
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )


    }
}