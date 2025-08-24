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

package com.example.feature.community.misc

import com.example.domain.model.supabase.Profile

fun formatUserRole(profile: Profile?): String {
    return when {
        profile == null -> ""
        profile.registeredAs == "Administrator" -> "[Administrator]"
        profile.registeredAs == "Expert" && profile.verified == true -> "[Expert]"
        profile.registeredAs == "Farmer" -> "Farmer"
        else -> ""
    }
}