package com.example.feature.community


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Reply

@Composable
fun ReplyThread(
    reply: Reply,
    replies: List<Reply>,
    onReplyClick: (Long) -> Unit,
    indent: Int = 0
) {
    var showReplies by remember { mutableStateOf(false) }

    val childReplies = replies.filter { it.parent_reply_id == reply.id }

    val parentReply = reply.parent_reply_id?.let { parentId ->
        replies.find { it.id == parentId }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (indent * 12).dp, top = 4.dp, bottom = 4.dp)
    ) {
        // Current reply
        Column(
            modifier = Modifier
                .clickable { onReplyClick(reply.id) }
        ) {
            // Sender name
            Text(
                text = reply.sender,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            // Reply bubble
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // If replying to someone, show as quote
                    parentReply?.let {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(IntrinsicSize.Min)
                                    .background(Color(0xFFCCCCCC), shape = RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(
                                modifier = Modifier
                                    .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = it.sender,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF555555)
                                )
                                Text(
                                    text = it.reply_body,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF333333),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Actual message
                    Text(
                        text = reply.reply_body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
            }
        }

        // Show "View Replies" button if this reply has children
        if (childReplies.isNotEmpty() && indent < 4) {
            TextButton(
                onClick = { showReplies = !showReplies },
                modifier = Modifier.padding(start = (indent * 12).dp)
            ) {
                Text(
                    text = if (showReplies) "Hide replies" else "View replies (${childReplies.size})",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // Render children only if showReplies is true
        if (showReplies) {
            childReplies.forEach { child ->
                ReplyThread(
                    reply = child,
                    replies = replies,
                    onReplyClick = onReplyClick,
                    indent = indent + 1
                )
            }
        }
    }
}