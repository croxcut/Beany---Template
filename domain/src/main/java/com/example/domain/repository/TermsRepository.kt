package com.example.domain.repository

import com.example.domain.model.Terms

interface TermsRepository {
    suspend fun getTerms(): Terms
}