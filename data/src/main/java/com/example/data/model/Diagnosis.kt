package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.data.local.misc.Converters
import com.example.domain.model.AABB
import java.util.Date

@Entity
@TypeConverters(Converters::class)
data class Diagnosis(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var imageUri: String = "",
    var boxes: List<AABB> = emptyList(),
    var lat: Double? = null,
    var long: Double? = null,
    var diagnosedAt: Date = Date() // <--- added field
)