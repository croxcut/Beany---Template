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

package com.example.data.repositoryImpl.local

import android.content.res.AssetManager
import com.example.domain.model.Terms
import com.example.domain.repository.local.TermsRepository
import kotlinx.serialization.json.Json

class TermsRepositoryImpl(
    private val assets: AssetManager,
    private val fileName: String = "beany_terms_conditions.json"
) : TermsRepository {
    override suspend fun getTerms(): Terms {
        val jsonString = assets.open(fileName).use { it.readBytes().decodeToString() }
        return Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
    }
}