package com.example.data.repositoryImpl

import com.example.data.local.dao.DiagnosisDao
import com.example.data.local.repository.DiagnosisRepository
import com.example.data.model.Diagnosis
import javax.inject.Inject

class DiagnosisRepositoryImpl @Inject constructor(
    private val dao: DiagnosisDao
) : DiagnosisRepository {

    override suspend fun saveDiagnosis(diagnosis: Diagnosis) {
        dao.insert(diagnosis)
    }

    override fun getAllDiagnoses() = dao.getAllDiagnoses()

    override suspend fun deleteDiagnosis(diagnosis: Diagnosis) {
        dao.deleteDiagnosis(diagnosis)
    }
}