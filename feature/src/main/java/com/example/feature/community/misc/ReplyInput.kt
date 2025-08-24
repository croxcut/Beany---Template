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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.domain.model.supabase.Profile
import com.example.domain.model.supabase.Reply

@Composable
fun ReplyInput(
    parentReplyId: Long?,
    replies: List<Reply>,
    profiles: List<Profile>,
    newReply: String,
    onNewReplyChange: (String) -> Unit,
    onSendReply: () -> Unit,
    onClearParent: () -> Unit // Add this parameter
) {
    Column {
        parentReplyId?.let { parentId ->
            replies.find { it.id == parentId }?.let { parent ->
                val parentProfile = profiles.find { it.id == parent.sender }
                ParentReplyPreview(
                    parent = parent,
                    profile = parentProfile,
                    onClose = onClearParent // Pass the close handler
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newReply,
                onValueChange = onNewReplyChange,
                textStyle = TextStyle(
                    fontSize = rspSp(15.sp),
                    color = Brown1,
                    fontFamily = GlacialIndifference
                ),
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = rspDp(2.dp),
                        color = Brown1,
                        shape = RoundedCornerShape(rspDp(50.dp))
                    )
                    .background(
                        color = Beige1,
                        shape = RoundedCornerShape(rspDp(50.dp))
                    )
                    .padding(end = 8.dp),
                placeholder = { Text(
                    text = "Write a reply...",
                    color = Brown1) },
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Brown1,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
            )

            Spacer(modifier = Modifier.width(5.dp))

            Button(
                onClick = onSendReply,
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Brown1
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send Reply",
                    tint = White
                )
            }
        }
    }
}