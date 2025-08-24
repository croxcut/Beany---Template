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

package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.data.local.misc.Converters
import com.example.domain.model.ml.AABB
import com.example.domain.model.db.Note
import java.util.Date

@Entity
@TypeConverters(Converters::class)
data class Diagnosis(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var imageUri: String = "",
    var boxes: List<AABB> = emptyList(),
    var lat: Double? = null,
    var long: Double? = null,
    var diagnosedAt: Date = Date(),
    var notes: List<Note> = emptyList()
)