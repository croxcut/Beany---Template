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

package com.example.feature.login.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.supabase.Profile
import com.example.domain.repository.remote.supabase.UpdateProfileRepository
import com.example.domain.repository.remote.supabase.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val updateProfileRepository: UpdateProfileRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<Profile>>(emptyList())
    val users: StateFlow<List<Profile>> = _users

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            val profiles = usersRepository.getProfiles()
            _users.value = profiles.filter { it.registeredAs == "Expert" || it.registeredAs == "Pathologist" }
        }
    }

    fun verifyUser(user: Profile) {
        viewModelScope.launch {
            val userId = user.id ?: return@launch
            _isUpdating.value = true

            val result = updateProfileRepository.updateVerified(userId, true)

            if (result.isSuccess) {
                fetchUsers()
            } else {

            }
            _isUpdating.value = false
        }
    }
}