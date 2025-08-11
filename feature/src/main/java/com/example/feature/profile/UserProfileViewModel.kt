package com.example.feature.profile


import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Profile
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.BucketRepository
import com.example.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository,
    private val bucketRepository: BucketRepository,
    private val appContext: Context
) : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    private val _isSignedUp = MutableStateFlow(false)
    val isSignedUp: StateFlow<Boolean> = _isSignedUp.asStateFlow()

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    init {
        viewModelScope.launch {
            _profile.value = sessionRepository.getUserProfile()
            _isSignedUp.value = sessionRepository.isSignedUp()
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionRepository.clearCurrentSession()
            authRepository.setLoggedIn(false)
        }
    }

    fun uploadProfileImage(uri: Uri) {
        viewModelScope.launch {
            try {
                val currentProfile = _profile.value
                    ?: throw IllegalStateException("No user profile loaded")

                _uploadState.value = UploadState.Loading

                val bytes = appContext.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalArgumentException("Unable to read file")

                val remotePath = "profiles/${currentProfile.id}.jpg"

                bucketRepository.upload(remotePath, bytes)

                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun downloadProfileImage() {
        viewModelScope.launch {
            try {
                val currentProfile = _profile.value
                    ?: throw IllegalStateException("No user profile loaded")

                val remotePath = "profiles/${currentProfile.id}.jpg"

                _uploadState.value = UploadState.Loading

                val imageBytes = try {
                    bucketRepository.getImage(remotePath)
                } catch (e: HttpRequestTimeoutException) {
                    _uploadState.value = UploadState.Error("Request timed out while downloading profile image.")
                    return@launch
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _uploadState.value = UploadState.Error("Failed to download profile image: ${e.message}")
                    return@launch
                }

                val tempFile = File(appContext.cacheDir, "profile_temp.jpg")
                tempFile.writeBytes(imageBytes)

                _selectedImageUri.value = tempFile.toUri()
                _uploadState.value = UploadState.Success

            } catch (e: IllegalStateException) {
                _uploadState.value = UploadState.Error(e.message ?: "Profile not loaded.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error("Unexpected error: ${e.message}")
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