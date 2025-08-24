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
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.utils.rspSp
import com.example.domain.model.supabase.Profile
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
                    fontFamily = Etna,
                    fontSize = rspSp(20.sp),
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
                    Text(
                        text = "Username: ",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp),
                            color = Brown1
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    profile.username?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(18.sp),
                                color = Brown1
                            )
                        )
                    }
                }

                Row {
                    Text(
                        text = "Name: ",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp),
                            color = Brown1
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    profile.fullName?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(18.sp),
                                color = Brown1
                            )
                        )
                    }
                }

                Row {
                    Text(
                        text = "Registered: ",
                        style = TextStyle(
                            fontFamily = GlacialIndifferenceBold,
                            fontSize = rspSp(18.sp),
                            color = Brown1
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    profile.registeredAs?.let { registeredAs ->
                        val status = if (registeredAs == "Administrator") {
                            ""
                        } else {
                            if (profile.verified == true) "[verified]" else "[pending]"
                        }
                        Text(
                            text = "$registeredAs$status",
                            style = TextStyle(
                                fontSize = rspSp(18.sp),
                                color = when {
                                    profile.verified != true -> Color.Gray
                                    registeredAs == "Administrator" -> Color.Red
                                    registeredAs == "Expert" -> Color.Blue
                                    else -> Color.Gray
                                },
                                fontFamily = GlacialIndifferenceBold
                            )
                        )
                    }
                }


                profile.province?.takeIf { profile.registeredAs != "Administrator" }?.let { province ->
                    Row {
                        Text(
                            text = "Province: ",
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(18.sp),
                                color = Brown1
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = province,
                            style = TextStyle(
                                fontFamily = GlacialIndifferenceBold,
                                fontSize = rspSp(18.sp),
                                color = Brown1
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Close",
                    style = TextStyle(
                        fontFamily = Etna,
                        fontSize = rspSp(18.sp),
                        color = Brown1
                    )
                )
            }
        },
        containerColor = Beige1
    )
}