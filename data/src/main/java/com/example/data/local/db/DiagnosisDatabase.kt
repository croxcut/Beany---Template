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

package com.example.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.dao.DiagnosisDao
import com.example.data.local.misc.Converters
import com.example.data.model.Diagnosis
@Database(
    entities = [Diagnosis::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // For storing lists like AABB
abstract class DiagnosisDatabase : RoomDatabase() {

    abstract fun diagnosisDao(): DiagnosisDao

    companion object {
        @Volatile
        private var INSTANCE: DiagnosisDatabase? = null

        fun getDatabase(context: Context): DiagnosisDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiagnosisDatabase::class.java,
                    "diagnosis_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}