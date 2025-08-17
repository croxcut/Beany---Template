package com.example.data.local.misc

import androidx.room.TypeConverter
import com.example.domain.model.AABB
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date


class Converters {
    private val gson = Gson()

    // AABB list converters
    @TypeConverter
    fun fromAABBList(boxes: List<AABB>): String = gson.toJson(boxes)

    @TypeConverter
    fun toAABBList(data: String): List<AABB> {
        val listType = object : TypeToken<List<AABB>>() {}.type
        return gson.fromJson(data, listType)
    }

    // Date converters
    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(timestamp: Long): Date = Date(timestamp)
}