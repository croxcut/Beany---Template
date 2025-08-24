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

package com.example.domain.model.supabase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String? = null,
    val username: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    val province: String? = null,
    val farm: String? = null,
    @SerialName("registered_as") val registeredAs: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    val verified: Boolean? = null
)