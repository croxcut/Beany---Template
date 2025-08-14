package com.example.beany

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.nav.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.slf4j.MDC.put

val supabase = createSupabaseClient(
    supabaseUrl = "https://moaafjxlduuwjpbgrheo.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1vYWFmanhsZHV1d2pwYmdyaGVvIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1NDM0NzMwMywiZXhwIjoyMDY5OTIzMzAzfQ.4QFmiQQMMsVmPJrt9o4R951OfNXRK-HEXjJkIA1r9JA"
) {
    install(Auth)
    install(Postgrest)
    install(Realtime)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(SupabaseExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val posts = remember { mutableStateListOf<PostWithReplies>() }
            var newReply by remember { mutableStateOf("") }
            var selectedPost by remember { mutableStateOf<PostWithReplies?>(null) }
            val coroutineScope = rememberCoroutineScope()

            // Load posts + replies
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    val postFlow: Flow<List<Post>> = supabase
                        .from("posts")
                        .selectAsFlow(Post::id)

                    val replyFlow: Flow<List<Reply>> = supabase
                        .from("replies")
                        .selectAsFlow(Reply::id)

                    combine(postFlow, replyFlow) { postList, replyList ->
                        postList.map { post ->
                            val postReplies = replyList.filter { it.post_id == post.id }
                            PostWithReplies(post, postReplies.toMutableStateList())
                        }
                    }.collect { combined ->
                        posts.clear()
                        posts.addAll(combined)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF0F0F0))
            ) {
                // Posts list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(posts) { postWithReplies ->
                        PostCard(
                            postWithReplies = postWithReplies,
                            isSelected = selectedPost?.post?.id == postWithReplies.post.id,
                            onSelect = { selectedPost = postWithReplies }
                        )
                    }
                }

                // Bottom reply bar (global)
                if (selectedPost != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = newReply,
                            onValueChange = { newReply = it },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            placeholder = { Text("Write a reply...") },
                            shape = RoundedCornerShape(24.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        Button(
                            onClick = {
                                if (newReply.isNotBlank()) {
                                    coroutineScope.launch {
                                        val reply = NewReply(
                                            post_id = selectedPost!!.post.id,
                                            sender = "me@example.com",
                                            reply_body = newReply
                                        )
                                        supabase.from("replies").insert(reply)
                                        newReply = ""
                                    }
                                }
                            },
                            shape = CircleShape,
                            modifier = Modifier.size(48.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Reply")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostCard(
    postWithReplies: PostWithReplies,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Sender name
            Text(postWithReplies.post.sender, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))

            // Post body
            Text(postWithReplies.post.post_body ?: "")
            Spacer(Modifier.height(8.dp))

            // Replies list as bubbles
            if (postWithReplies.replies.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    postWithReplies.replies.forEach { reply ->
                        val isMine = reply.sender == "me@example.com"
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = if (isMine) Alignment.CenterEnd else Alignment.CenterStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (isMine) Color(0xFFDCF8C6) else Color(0xFFE5E5EA),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = reply.reply_body,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            if (isSelected) {
                Text(
                    "Replying...",
                    color = Color(0xFF0B93F6),
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Serializable
data class Post(
    val id: Long,
    val sender: String,
    val post_body: String? = null,
    val created_at: String
)

@Serializable
data class Reply(
    val id: Long,
    val post_id: Long,
    val sender: String,
    val reply_body: String,
    val created_at: String
)

data class PostWithReplies(
    val post: Post,
    val replies: MutableList<Reply>
)

@Serializable
data class NewPost(
    val sender: String,
    val post_body: String
)

@Serializable
data class NewReply(
    val post_id: Long,
    val sender: String,
    val reply_body: String
)