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

package com.example.data.repositoryImpl.local.db

import com.example.data.local.dao.ActivityDao
import com.example.data.model.ActivityEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRepository @Inject constructor(
    private val dao: ActivityDao
) {
    suspend fun insert(activity: String) {
        dao.insertActivity(ActivityEntity(activity = activity))
    }

    fun getAll(): Flow<List<ActivityEntity>> = dao.getAllActivities()

    suspend fun clearAll() {
        dao.clearAll()
    }
}