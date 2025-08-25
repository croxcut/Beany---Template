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

package com.example.feature.profile.updateProfile

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.weather.City
import com.example.domain.model.supabase.Profile
import com.example.domain.repository.remote.supabase.BucketRepository
import com.example.domain.repository.remote.supabase.SessionRepository
import com.example.domain.repository.remote.supabase.UpdateProfileRepository
import com.example.domain.repository.remote.openMeteo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val updateProfileRepository: UpdateProfileRepository,
    private val sessionRepository: SessionRepository,
    private val bucketRepository: BucketRepository,
    private val weatherRepository: WeatherRepository,
    private val context: Context
) : ViewModel() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _credentialUri = MutableStateFlow<Uri?>(null)
    val credentialUri: StateFlow<Uri?> = _credentialUri

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    init {
        viewModelScope.launch {
            sessionRepository.getUserProfile()?.let { profile ->
                _userId.value = profile.id.orEmpty()
                _uiState.value = _uiState.value.copy(profile = profile)
                loadCredential(profile.id.orEmpty())
                loadCities()
            }
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            try {
                _cities.value = weatherRepository.getCities()
            } catch (e: Exception) {
            }
        }
    }

    private fun loadCredential(userId: String) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            try {
                val remotePath = "credentials/$userId.jpg"
                val bytes = bucketRepository.getImage(remotePath)
                val tempFile = File(context.cacheDir, "credential_temp.jpg")
                tempFile.writeBytes(bytes)
                _credentialUri.value = tempFile.toUri()
                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Failed to load credential")
            }
        }
    }

    fun uploadCredential(uri: Uri) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            try {
                val userId = _userId.value
                val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalArgumentException("Failed to read image")

                bucketRepository.upload("credentials/$userId.jpg", bytes)
                _credentialUri.value = uri
                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Upload failed")
            }
        }
    }

    fun onProfileChange(updated: Profile) {
        _uiState.value = _uiState.value.copy(profile = updated)
    }

    fun updateProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, success = false)
            val result = updateProfileRepository.updateProfile(_uiState.value.profile)
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(isLoading = false, success = true)
            } else {
                _uiState.value.copy(isLoading = false, error = result.exceptionOrNull()?.message)
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