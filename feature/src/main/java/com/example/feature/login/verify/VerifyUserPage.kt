package com.example.feature.login.verify


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.supabase.Profile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyUserPage(viewModel: UsersViewModel = hiltViewModel()) {
    val users by viewModel.users.collectAsState()
    val isUpdating by viewModel.isUpdating.collectAsState()

    var selectedUser by remember { mutableStateOf<Profile?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Experts & Pathologists") }) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(users) { user ->
                    UserItem(user, onVerifyClick = {
                        selectedUser = user
                        showDialog = true
                    })
                }
            }

            if (showDialog && selectedUser != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Verify User") },
                    text = { Text("Are you sure you want to verify ${selectedUser!!.username}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.verifyUser(selectedUser!!)
                            showDialog = false
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (isUpdating) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize())
            }
        }
    }
}

@Composable
fun UserItem(user: Profile, onVerifyClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Registered As: ${user.registeredAs ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Verified: ${user.verified ?: false}", style = MaterialTheme.typography.bodyMedium)
            if (user.verified != true) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onVerifyClick) {
                    Text("Verify")
                }
            }
        }
    }
}