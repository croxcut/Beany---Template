package com.example.data.local.misc

import androidx.room.TypeConverter
import com.example.domain.model.ml.AABB
import com.example.domain.model.db.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date


class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromAABBList(boxes: List<AABB>): String = gson.toJson(boxes)

    @TypeConverter
    fun toAABBList(data: String): List<AABB> {
        return gson.fromJson(data, object : TypeToken<List<AABB>>() {}.type)
    }

    @TypeConverter
    fun fromNoteList(notes: List<Note>): String = gson.toJson(notes)

    @TypeConverter
    fun toNoteList(data: String): List<Note> {
        return gson.fromJson(data, object : TypeToken<List<Note>>() {}.type)
    }

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(timestamp: Long): Date = Date(timestamp)

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}