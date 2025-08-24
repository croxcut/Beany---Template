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

package com.example.feature.community.misc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.utils.rspDp
import com.example.domain.model.supabase.Profile
import com.example.domain.model.supabase.Reply

@Composable
fun ParentReplyPreview(
    parent: Reply,
    profile: Profile?,
    onClose: () -> Unit // Add close handler
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E5EA))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val displayName = profile?.username ?: "Unknown"
                    val userRole = formatUserRole(profile)

                    Text("$displayName:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    if (userRole.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = userRole,
                            color = if (userRole == "[Administrator]") Color.Red else Color.Gray,
                            fontSize = 10.sp
                        )
                    }
                }
                Text(parent.reply_body, fontSize = 14.sp)
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .padding(rspDp(5.dp))
                    .align(Alignment.TopEnd)
                    .size(rspDp(20.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun ParentReplyInThread(
    parent: Reply,
    profile: Profile?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = profile?.username ?: "Unknown",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatUserRole(profile),
                    fontSize = 10.sp,
                    color = when {
                        profile?.verified != true -> Color.Gray
                        profile?.registeredAs == "Administrator" -> Color.Red
                        profile?.registeredAs == "Expert" -> Color.Blue
                        else -> Color.Gray
                    }
                )
            }
            Text(
                text = parent.reply_body ?: "",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}