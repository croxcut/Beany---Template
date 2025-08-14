package com.example.data.repositoryImpl

import android.content.res.AssetManager
import com.example.domain.model.Terms
import com.example.domain.repository.TermsRepository
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
