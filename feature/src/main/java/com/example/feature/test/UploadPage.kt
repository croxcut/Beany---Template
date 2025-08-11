package com.example.feature.test

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

@Composable
fun UploadPage(viewModel: UploadViewModel = hiltViewModel()) {
    val uploadState by viewModel.uploadState.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    // Step 1: Image cropper launcher
    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { uri ->
                viewModel.uploadImage(uri)
            }
        } else {
            result.error?.printStackTrace()
        }
    }

    // Step 2: File picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                cropImageLauncher.launch(
                    CropImageContractOptions(
                        uri,
                        CropImageOptions(
                            guidelines = CropImageView.Guidelines.ON,
                            cropShape = CropImageView.CropShape.RECTANGLE,
                            fixAspectRatio = true,
                            activityTitle = "Crop Image",
                            activityMenuIconColor = android.graphics.Color.WHITE, // Makes icons visible
                            toolbarColor = android.graphics.Color.BLACK,          // Toolbar background
                            allowRotation = true,
                            allowFlipping = true
                        )
                    )
                )
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            enabled = uploadState !is UploadState.Loading
        ) {
            Text("Select & Crop Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uploadState) {
            is UploadState.Idle -> Text("Waiting for file...")
            is UploadState.Loading -> CircularProgressIndicator()
            is UploadState.Success -> Text("✅ Upload successful!")
            is UploadState.Error -> Text("❌ Error: ${(uploadState as UploadState.Error).message}")
        }

        Button(onClick = { viewModel.downloadImage("uploads/1754793143803.jpg") }) {
            Text("Download & Show Image")
        }

        selectedImageUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Downloaded Image",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}