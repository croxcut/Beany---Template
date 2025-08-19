package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TermSection(
    val number: Int,
    val title: String,
    val content: String
)