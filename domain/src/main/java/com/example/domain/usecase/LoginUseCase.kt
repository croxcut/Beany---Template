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

package com.example.domain.usecase

import com.example.domain.model.supabase.LoginCredential
import com.example.domain.repository.remote.supabase.LoginAuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginAuthRepository: LoginAuthRepository
) {
    suspend operator fun invoke(loginCredential: LoginCredential): Result<Unit> {
        return loginAuthRepository.login(loginCredential)
    }
}