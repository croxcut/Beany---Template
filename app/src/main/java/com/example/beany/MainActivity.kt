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
import androidx.compose.material.icons.filled.Close
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
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
            val navController = rememberNavController()
            NavGraph(navController, this)
//            NavHost(
//                navController = navController,
//                startDestination = "posts_list"
//            ) {
//                composable("posts_list") {
//                    PostsListScreen(
//                        onPostClick = { postId ->
//                            navController.navigate("post_detail/$postId")
//                        }
//                    )
//                }
//                composable("post_detail/{postId}") { backStackEntry ->
//                    val postId = backStackEntry.arguments?.getString("postId")?.toLongOrNull()
//                    if (postId != null) {
//                        PostDetailScreen(postId = postId)
//                    }
//                }
//            }
        }
    }
}

@OptIn(SupabaseExperimental::class)
@Composable
fun PostsListScreen(onPostClick: (Long) -> Unit) {
    val posts = remember { mutableStateListOf<Post>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val postFlow: Flow<List<Post>> = supabase
                .from("posts")
                .selectAsFlow(Post::id)

            postFlow.collect { postList ->
                posts.clear()
                posts.addAll(postList)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(posts) { post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onPostClick(post.id) },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(post.sender, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(post.post_body ?: "")
                    }
                }
            }
        }
    }
}

@OptIn(SupabaseExperimental::class)
@Composable
fun PostDetailScreen(postId: Long) {
    val coroutineScope = rememberCoroutineScope()
    var post by remember { mutableStateOf<Post?>(null) }
    val replies = remember { mutableStateListOf<Reply>() }
    var newReply by remember { mutableStateOf("") }
    var parentReplyId by remember { mutableStateOf<Long?>(null) }

    // Load post
    LaunchedEffect(postId) {
        coroutineScope.launch {
            val postsList: List<Post> = supabase
                .from("posts")
                .selectAsFlow(
                    Post::id,
                    filter = FilterOperation("id", FilterOperator.EQ, postId)
                )
                .first()
            post = postsList.firstOrNull()
        }
    }

    // Load replies
    LaunchedEffect(postId) {
        coroutineScope.launch {
            val replyFlow: Flow<List<Reply>> = supabase
                .from("replies")
                .selectAsFlow(
                    Reply::id,
                    filter = FilterOperation("post_id", FilterOperator.EQ, postId)
                )

            replyFlow.collect { replyList ->
                replies.clear()
                replies.addAll(replyList)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Post card at top
            item {
                post?.let {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(it.sender, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(it.post_body ?: "")
                        }
                    }
                }
            }

            // Recursive reply rendering
            items(replies.filter { it.parent_reply_id == null }, key = { it.id }) { reply ->
                ReplyThread(
                    reply = reply,
                    replies = replies,
                    onReplyClick = { parentReplyId = it }
                )
            }
        }

        // Reply input bar
        Column {
            if (parentReplyId != null) {
                val parent = replies.find { it.id == parentReplyId }
                if (parent != null) {
                    Text(
                        text = "Replying to: ${parent.sender}",
                        color = Color.Blue,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
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
                                    post_id = postId,
                                    sender = "clme@example.com",
                                    reply_body = newReply,
                                    parent_reply_id = parentReplyId
                                )
                                supabase.from("replies").insert(reply)
                                newReply = ""
                                parentReplyId = null
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
        Box(
            modifier = Modifier
                .background(
                    color = if (indent == 0) Color(0xFFE5E5EA) else Color(0xFFD8EAFB),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(reply.reply_body)
        }

        // Recursively render child replies
        replies.filter { it.parent_reply_id == reply.id }.forEach { child ->
            ReplyThread(child, replies, onReplyClick, indent + 1)
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
    val created_at: String,
    val parent_reply_id: Long? = null // NEW
)

@Serializable
data class NewReply(
    val post_id: Long,
    val sender: String,
    val reply_body: String,
    val parent_reply_id: Long? = null // NEW
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
