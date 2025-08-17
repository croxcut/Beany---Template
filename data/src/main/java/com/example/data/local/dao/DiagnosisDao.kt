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