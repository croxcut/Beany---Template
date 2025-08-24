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

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val sender: String,
    val post_title: String,
    val post_body: String? = null,
    val image_url: String? = null,
    val created_at: String,
    val tags: List<String>? = emptyList(),
    val likes: List<String>? = emptyList()
)