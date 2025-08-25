// ===============================================================================
//
// Copyright (C) 2025-2026 by John Paul Valenzuela
//
// This source is available for distribution and/or modification
// only under the terms of the Beany Source Code License as
// published by Beany. All rights reserved.
//
// The source is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// FITNESS FOR A PARTICULAR PURPOSE. See the Beany Source Code License
// for more details.
//
// ===============================================================================

package com.example.feature.profile


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.network.NetworkUtils
import com.example.domain.model.supabase.Profile
import com.example.domain.repository.local.datastore.AuthRepository
import com.example.domain.repository.remote.supabase.BucketRepository
import com.example.domain.repository.remote.supabase.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.Dispatchers
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


    private val _isOnline = MutableStateFlow(NetworkUtils.isInternetAvailable(appContext))
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        checkConnectivity()
        if(isOnline.value) {
            loadProfileData()
        }
    }

    fun refreshData() {
        checkConnectivity()
        if(isOnline.value) {
            loadProfileData()
            downloadProfileImage()
        }
    }

    fun initializeData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                refreshSession()
                _profile.value = sessionRepository.getUserProfile()
                _isSignedUp.value = sessionRepository.isSignedUp()
                if (_isSignedUp.value) {
                    downloadProfileImage()
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Failed to fetch profile data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadProfileData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _profile.value = sessionRepository.getUserProfile()
                _isSignedUp.value = sessionRepository.isSignedUp()
            } catch (e: Exception) {
                Log.e("ViewModel", "Failed to fetch profile data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun refreshSession() {
        viewModelScope.launch {
            sessionRepository.updateCurrentSession()
        }
    }

    fun checkConnectivity() {
        _isOnline.value = NetworkUtils.isInternetAvailable(appContext)
    }

    fun logout() {
        viewModelScope.launch {
            sessionRepository.clearCurrentSession()
            authRepository.setLoggedIn(false)
        }
    }

    fun uploadProfileImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentProfile = _profile.value
                    ?: throw IllegalStateException("No user profile loaded")

                _uploadState.value = UploadState.Loading

                val bytes = appContext.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalArgumentException("Unable to read file")

                val remotePath = "profiles/${currentProfile.id}.jpg"

                bucketRepository.upload(remotePath, bytes)

                _selectedImageUri.value = uri

                sessionRepository.updateCurrentSession()

                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                Log.e("ProfileUpload", "Upload failed", e)
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