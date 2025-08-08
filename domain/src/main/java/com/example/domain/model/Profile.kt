package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class Profile(
    val id: String? = null,
    val username: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    val province: String? = null,
    val farm: String? = null,
    @SerialName("registered_as") val registeredAs: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)