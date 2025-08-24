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

package com.example.feature.signup

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object PendingVerification : SignUpState()  // New state
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}