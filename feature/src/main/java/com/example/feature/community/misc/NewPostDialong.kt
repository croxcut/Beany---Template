package com.example.feature.community.misc

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.core.composables.InputField
import com.example.core.ui.theme.Beige1
import com.example.core.ui.theme.Brown1
import com.example.core.utils.rspDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostDialog(
    onDismiss: () -> Unit,
    onPost: (String, String, List<String>, Uri?) -> Unit,
    availableTags: List<String>,
    selectedTags: List<String>,
    onTagsChanged: (List<String>) -> Unit,
    title: String,
    onTitleChanged: (String) -> Unit,
    body: String,
    onBodyChanged: (String) -> Unit,
    selectedImage: Uri?,
    onImageSelected: (Uri?) -> Unit
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    // Full screen dialog
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Beige1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = rspDp(10.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {

                // Title field
                InputField(
                    value = title,
                    onValueChange = onTitleChanged,
                    label = {
                        Text(
                            text = "Title",
                            style = TextStyle(
                                color = Brown1
                            ),
                        )
                    },
                    textStyle = TextStyle(
                        color = Brown1
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Tags
                Text("Tags:", modifier = Modifier.padding(bottom = 2.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    availableTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                onTagsChanged(if (isSelected) selectedTags - tag else selectedTags + tag)
                            },
                            label = { Text(tag) },
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Body field
                InputField(
                    value = body,
                    onValueChange = onBodyChanged,
                    label = {
                        Text(
                            text = "Body",
                            style = TextStyle(
                                color = Brown1
                            ),
                        )
                    },
                    textStyle = TextStyle(
                        color = Brown1
                    ),
                    maxLines = 5,
                    singleLine = false
                )

                Spacer(modifier = Modifier.height(16.dp))
                // Image picker row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = Brown1)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add image")
                    }

                    selectedImage?.let { uri ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { onImageSelected(null) }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Remove image")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(contentColor = Brown1)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            if (title.isNotBlank() && body.isNotBlank()) {
                                onPost(title, body, selectedTags, selectedImage)
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Brown1)
                    ) {
                        Text("Post")
                    }
                }
            }
        }
    }
}