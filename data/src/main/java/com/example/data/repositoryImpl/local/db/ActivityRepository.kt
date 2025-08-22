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