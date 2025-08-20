package com.example.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.dao.ActivityDao
import com.example.data.local.misc.Converters
import com.example.data.model.ActivityEntity

@Database(entities = [ActivityEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ActivityDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}