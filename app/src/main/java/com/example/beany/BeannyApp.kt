package com.example.beany

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BeanyApp : Application()

//@Composable
//fun PostCard(
//    postWithReplies: PostWithReplies,
//    isSelected: Boolean,
//    onSelect: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//            .clickable { onSelect() },
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//        Column(modifier = Modifier.padding(12.dp)) {
//            // Sender name
//            Text(postWithReplies.post.sender, fontWeight = FontWeight.Bold)
//            Spacer(Modifier.height(4.dp))
//
//            // Post body
//            Text(postWithReplies.post.post_body ?: "")
//            Spacer(Modifier.height(8.dp))
//
//            // Replies list as bubbles
//            if (postWithReplies.replies.isNotEmpty()) {
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.spacedBy(6.dp)
//                ) {
//                    postWithReplies.replies.forEach { reply ->
//                        val isMine = reply.sender == "me@example.com"
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            contentAlignment = if (isMine) Alignment.CenterEnd else Alignment.CenterStart
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .background(
//                                        color = if (isMine) Color(0xFFDCF8C6) else Color(0xFFE5E5EA),
//                                        shape = RoundedCornerShape(16.dp)
//                                    )
//                                    .padding(horizontal = 12.dp, vertical = 8.dp)
//                            ) {
//                                Text(
//                                    text = reply.reply_body,
//                                    color = Color.Black
//                                )
//                            }
//                        }
//                    }
//                }
//                Spacer(Modifier.height(8.dp))
//            }
//
//            if (isSelected) {
//                Text(
//                    "Replying...",
//                    color = Color(0xFF0B93F6),
//                    fontSize = 12.sp,
//                    modifier = Modifier.align(Alignment.End)
//                )
//            }
//        }
//    }
//}