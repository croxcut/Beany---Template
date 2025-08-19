package com.example.feature.community.misc

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.domain.model.Profile
import com.example.feature.R

@Composable
fun ProfileDialog(
    profile: Profile,
    imageUri: Uri?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Profile Info",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Brown1
                )
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Profile Picture",
                    placeholder = painterResource(R.drawable.plchldr),
                    error = painterResource(R.drawable.plchldr),
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Brown1, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text("Username: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(profile.username ?: "Unknown", fontWeight = FontWeight.Bold)
                }

                Row {
                    Text("Name: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(profile.fullName ?: "Unknown", fontWeight = FontWeight.Bold)
                }

                Row {
                    Text("Registered: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    profile.registeredAs?.let { registeredAs ->
                        val status = if (registeredAs == "Administrator") {
                            ""
                        } else {
                            if (profile.verified == true) "[verified]" else "[pending]"
                        }
                        Text(
                            text = "$registeredAs$status",
                            color = when {
                                profile.verified != true -> Color.Gray
                                registeredAs == "Administrator" -> Color.Red
                                registeredAs == "Expert" -> Color.Blue
                                else -> Color.Gray
                            }
                        )
                    }
                }

                profile.province?.takeIf { profile.registeredAs != "Administrator" }?.let { province ->
                    Row {
                        Text("Province: ", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(province)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        containerColor = Beige1
    )
}