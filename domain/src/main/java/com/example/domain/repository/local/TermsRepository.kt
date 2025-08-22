package com.example.domain.repository.local

import com.example.domain.model.Terms

interface TermsRepository {
    suspend fun getTerms(): Terms
}