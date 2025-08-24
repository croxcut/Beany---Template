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

package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.model.Diagnosis
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisDao {

    @Insert
    suspend fun insert(diagnosis: Diagnosis)

    @Query("SELECT * FROM diagnosis")
    fun getAllDiagnoses(): Flow<List<Diagnosis>>

    @Delete
    suspend fun deleteDiagnosis(diagnosis: Diagnosis)
}