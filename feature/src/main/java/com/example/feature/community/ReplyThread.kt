package com.example.feature.community


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.model.Reply

@Composable
fun ReplyThread(
    reply: Reply,
    replies: List<Reply>,
    onReplyClick: (Long) -> Unit,
    indent: Int = 0
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (indent * 24).dp)
            .clickable { onReplyClick(reply.id) }
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (indent == 0) Color(0xFFE5E5EA) else Color(0xFFD8EAFB)
            )
        ) {
            Text(
                text = reply.reply_body,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                color = Color.Black
            )
        }

        replies
            .filter { it.parent_reply_id == reply.id }
            .forEach { child ->
                ReplyThread(
                    reply = child,
                    replies = replies,
                    onReplyClick = onReplyClick,
                    indent = indent + 1
                )
            }
    }
}