package com.example.feature.test

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.BucketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val bucketRepository: BucketRepository,
    private val appContext: Context
) : ViewModel() {

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Loading

                val bytes = appContext.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalArgumentException("Unable to read file")

                val remotePath = "uploads/${System.currentTimeMillis()}.jpg"

                bucketRepository.upload(remotePath, bytes)

                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun downloadImage(remotePath: String) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Loading
                val imageBytes = bucketRepository.getImage(remotePath)

                val tempFile = File(appContext.cacheDir, "downloaded.jpg")
                tempFile.writeBytes(imageBytes)

                _selectedImageUri.value = tempFile.toUri()
                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}
