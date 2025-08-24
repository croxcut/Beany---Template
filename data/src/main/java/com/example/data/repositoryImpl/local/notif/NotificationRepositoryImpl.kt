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

package com.example.data.repositoryImpl.local.notif

import com.example.data.local.dao.NotificationDao
import com.example.data.local.misc.toDomain
import com.example.data.local.misc.toEntity
import com.example.data.local.repository.NotificationRepository
import com.example.domain.model.ScheduledNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class NotificationRepositoryImpl @Inject constructor(
    private val dao: NotificationDao
) : NotificationRepository {

    override suspend fun insert(notification: ScheduledNotification) {
        dao.insert(notification.toEntity())
    }

    override suspend fun delete(id: Int) {
        // You don’t need to build a full entity with dummy values.
        // Just let your DAO delete by ID.
        dao.deleteById(id)
    }

    override fun getAllNotifications(): Flow<List<ScheduledNotification>> {
        return dao.getAll().map { list ->
            list.map { it.toDomain() }
        }
    }
}