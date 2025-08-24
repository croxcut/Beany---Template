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

package com.example.domain.model.ml

data class AABB(
    val xpos_1: Float,
    val ypos_1: Float,
    val xpos_2: Float,
    val ypos_2: Float,
    val center_x: Float,
    val center_y: Float,
    val width: Float,
    val height: Float,
    val confidence_score: Float,
    val class_index: Int,
    val class_name: String
)