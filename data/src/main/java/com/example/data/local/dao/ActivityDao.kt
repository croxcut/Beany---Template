package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.model.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert
    suspend fun insertActivity(entity: ActivityEntity)

    @Query("SELECT * FROM activity ORDER BY date DESC")
    fun getAllActivities(): Flow<List<ActivityEntity>>
    @Query("DELETE FROM activity")
    suspend fun clearAll()
}