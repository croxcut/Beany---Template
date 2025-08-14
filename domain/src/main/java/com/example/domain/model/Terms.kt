package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Terms(
    val title: String,
    val intro: String,
    val sections: List<TermSection>
)

