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

package com.example.data.repositoryImpl.local.ml

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