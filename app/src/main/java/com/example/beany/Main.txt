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
import androidx.compose.material.icons.filled.Delete
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
import io.github.jan.supabase.postgrest.query.filter.FilterOperator.EQ
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
import java.util.Date

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
//            NavGraph(navController, this)
            NavHost(
                navController = navController,
                startDestination = "posts_list"
            ) {
                composable("posts_list") {
                    PostsListScreen(
                        onPostClick = { postId ->
                            navController.navigate("post_detail/$postId")
                        }
                    )
                }
                composable("post_detail/{postId}") { backStackEntry ->
                    val postId = backStackEntry.arguments?.getString("postId")?.toLongOrNull()
                    if (postId != null) {
                        PostDetailScreen(postId = postId)
                    }
                }
            }
        }
    }
}

@OptIn(SupabaseExperimental::class)
@Composable
fun PostsListScreen(onPostClick: (Long) -> Unit) {
    val posts = remember { mutableStateListOf<Post>() }
    val coroutineScope = rememberCoroutineScope()
    var newPostBody by remember { mutableStateOf("") }

    var postToDelete by remember { mutableStateOf<Post?>(null) } // For dialog

    // Sorting state
    var expanded by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf("Date") } // default

    // Load posts
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val postFlow: Flow<List<Post>> = supabase.from("posts").selectAsFlow(Post::id)
                postFlow.collect { postList ->
                    posts.clear()
                    posts.addAll(postList)
                }
            } catch (e: Exception) {
                println("Error loading posts: ${e.message}")
                // You could show a snackbar or error message to the user here
            }
        }
    }

    // Sort posts whenever posts content or sortOption changes
    val sortedPosts by remember(posts, sortOption) {
        derivedStateOf {
            when (sortOption) {
                "Date" -> posts.sortedByDescending { it.created_at } // latest first
                "Name" -> posts.sortedBy { it.sender }
                else -> posts
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {
        // Dropdown for sorting
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(12.dp)
        ) {
            Text("Sort by: $sortOption")
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Date") },
                    onClick = {
                        sortOption = "Date"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Name") },
                    onClick = {
                        sortOption = "Name"
                        expanded = false
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(sortedPosts) { post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onPostClick(post.id) }
                        ) {
                            Text(post.sender, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(post.post_body ?: "")
                        }

                        // Delete Icon
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Delete Post",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { postToDelete = post }
                        )
                    }
                }
            }
        }

        var newPostTitle by remember { mutableStateOf("") }
        TextField(
            value = newPostTitle,
            onValueChange = { newPostTitle = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            placeholder = { Text("Post title...") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        // Input bar for new post
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newPostBody,
                onValueChange = { newPostBody = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("Write a new post...") },
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Button(
                onClick = {
                    if (newPostTitle.isNotBlank() && newPostBody.isNotBlank()) {
                        coroutineScope.launch {
                            try {
                                val tempPost = Post(
                                    id = System.currentTimeMillis(),
                                    sender = "clme@example.com",
                                    post_title = newPostTitle, // NEW: Include title
                                    post_body = newPostBody,
                                    created_at = Date().toString()
                                )
                                posts.add(0, tempPost)

                                val newPost = NewPost(
                                    sender = "clme@example.com",
                                    post_title = newPostTitle, // NEW: Include title
                                    post_body = newPostBody
                                )
                                supabase.from("posts").insert(newPost)

                                newPostTitle = ""
                                newPostBody = ""
                            } catch (e: Exception) {
                                println("Error creating post: ${e.message}")
                            }
                        }
                    }
                },
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Post")
            }
        }
    }

    // Confirmation dialog
    postToDelete?.let { post ->
        AlertDialog(
            onDismissRequest = { postToDelete = null },
            title = { Text("Delete Post") },
            text = { Text("Are you sure you want to delete this post?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                deletePost(post.id)
                                posts.remove(post)
                            } catch (e: Exception) {
                                println("Error deleting post: ${e.message}")
                                // Show error to user
                            } finally {
                                postToDelete = null
                            }
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { postToDelete = null }
                ) {
                    Text("No")
                }
            }
        )
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
            try {
                val postsList: List<Post> = withContext(Dispatchers.IO) {
                    supabase.from("posts")
                        .selectAsFlow(Post::id, filter = FilterOperation("id", FilterOperator.EQ, postId))
                        .first()
                }
                post = postsList.firstOrNull()
            } catch (e: Exception) {
                println("Failed to fetch post: ${e.message}")
                // Show error to user
            }
        }
    }

    // Load replies with error handling
    LaunchedEffect(postId) {
        coroutineScope.launch {
            try {
                val replyFlow: Flow<List<Reply>> = supabase.from("replies")
                    .selectAsFlow(Reply::id, filter = FilterOperation("post_id", FilterOperator.EQ, postId))

                replyFlow.collect { list ->
                    replies.clear()
                    replies.addAll(list)
                }
            } catch (e: Exception) {
                println("Failed to fetch replies: ${e.message}")
                // Show error to user
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
                post?.let { postItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(postItem.sender, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(postItem.post_title, fontWeight = FontWeight.SemiBold) // NEW: Show title
                            Spacer(Modifier.height(8.dp))
                            Text(postItem.post_body ?: "")
                            Spacer(Modifier.height(8.dp))

                            // Now all replies are inside this post bubble
                            replies.filter { it.parent_reply_id == null }.forEach { reply ->
                                ReplyThread(
                                    reply = reply,
                                    replies = replies,
                                    onReplyClick = { parentReplyId = it },
                                    indent = 1 // start indent inside post bubble
                                )
                            }
                        }
                    }
                }
            }
        }

        // Reply input
        Column {
            parentReplyId?.let { parentId ->
                val parent = replies.find { it.id == parentId }
                parent?.let {
                    // Show the message being replied to above input
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E5EA))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("${it.sender}:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(it.reply_body, fontSize = 14.sp)
                        }
                    }
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
                                try {
                                    val reply = NewReply(
                                        post_id = postId,
                                        sender = "clme@example.com",
                                        reply_body = newReply,
                                        parent_reply_id = parentReplyId
                                    )
                                    supabase.from("replies").insert(reply)
                                    newReply = ""
                                    parentReplyId = null
                                } catch (e: Exception) {
                                    println("Error creating reply: ${e.message}")
                                    // Show error to user
                                }
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

@OptIn(SupabaseExperimental::class)
@Composable
fun ReplyThread(
    reply: Reply,
    replies: List<Reply>,
    onReplyClick: (Long) -> Unit,
    indent: Int
) {
    val maxIndent = 4
    val actualIndent = minOf(indent, maxIndent)
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }

    // Don't show the icon if the message is already unsent
    val canUnsend = reply.reply_body != "Unsent a message"

    // Find the parent reply, if any
    val parentReply = reply.parent_reply_id?.let { parentId ->
        replies.find { it.id == parentId }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (actualIndent * 12).dp, end = 4.dp)
            .clickable { onReplyClick(reply.id) }
    ) {
        // Show parent reply outside the bubble
        parentReply?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .offset(x = -10.dp, y = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E5EA))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(it.sender, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(it.reply_body, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        // Bubble for the current reply
        Box(
            modifier = Modifier
                .background(
                    color = if (actualIndent == 0) Color(0xFFD8EAFB) else Color(0xFFF1F0F0),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(reply.sender, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(reply.reply_body, fontSize = 14.sp)
                }

                if (canUnsend) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Unsend Reply",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { showDialog = true },
                        tint = Color.Red
                    )
                }
            }
        }

        // Confirmation dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Unsend Message") },
                text = { Text("Do you want to unsend this message?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    unsendReply(reply.id)
                                } catch (e: Exception) {
                                    println("Error unsending reply: ${e.message}")
                                    // Show error to user
                                } finally {
                                    showDialog = false
                                }
                            }
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("No")
                    }
                }
            )
        }

        // Render child replies recursively
        replies.filter { it.parent_reply_id == reply.id }.forEach { child ->
            ReplyThread(child, replies, onReplyClick, indent + 1)
        }
    }
}
@OptIn(SupabaseExperimental::class)
suspend fun unsendReply(replyId: Long) {
    try {
        supabase.from("replies").update(
            mapOf(
                "reply_body" to "Unsent a message"
            )
        ) {
            filter {
                eq("id", replyId)
            }
        }
    } catch (e: Exception) {
        println("Failed to unsend reply: ${e.message}")
    }
}

@OptIn(SupabaseExperimental::class)
suspend fun deletePost(postId: Long) {
    try {
        // First delete all replies associated with this post
        supabase.from("replies").delete {
            filter { eq("post_id", postId) }
        }

        // Then delete the post itself
        supabase.from("posts").delete {
            filter { eq("id", postId) }
        }
    } catch (e: Exception) {
        println("Failed to delete post: ${e.message}")
    }
}

@Serializable
data class Post(
    val id: Long,
    val sender: String,
    val post_title: String, // NEW: Added post title
    val post_body: String? = null,
    val image_url: String? = null,
    val created_at: String
)

@Serializable
data class NewPost(
    val sender: String,
    val post_title: String, // NEW: Added post title
    val post_body: String,
    val image_url: String? = null
)
@Serializable
data class Reply(
    val id: Long,
    val post_id: Long,
    val sender: String,
    val reply_body: String,
    val image_url: String? = null, // NEW
    val created_at: String,
    val parent_reply_id: Long? = null
)

@Serializable
data class NewReply(
    val post_id: Long,
    val sender: String,
    val reply_body: String,
    val image_url: String? = null, // NEW
    val parent_reply_id: Long? = null
)
