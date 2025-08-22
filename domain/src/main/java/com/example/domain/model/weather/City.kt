package com.example.domain.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double
)