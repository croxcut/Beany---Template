package com.example.domain.usecase

import com.example.domain.model.Terms
import com.example.domain.repository.local.TermsRepository

class GetTermsUseCase(private val repo: TermsRepository) {
    suspend operator fun invoke(): Terms = repo.getTerms()
}