package com.example.data.local.repository

import com.example.data.model.Diagnosis
import kotlinx.coroutines.flow.Flow

interface DiagnosisRepository {
    suspend fun saveDiagnosis(diagnosis: Diagnosis)
    fun getAllDiagnoses(): Flow<List<Diagnosis>>
    suspend fun deleteDiagnosis(diagnosis: Diagnosis)
}