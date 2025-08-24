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

fun formatDate(dateString: String): String {
    return try {
        val parts = dateString.split("T")[0].split("-")
        "${parts[1]}/${parts[2]}/${parts[0]}"
    } catch (e: Exception) {
        dateString
    }
}