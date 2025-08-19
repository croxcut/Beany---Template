package com.example.feature.community.misc

import com.example.domain.model.Profile

fun formatUserRole(profile: Profile?): String {
    return when {
        profile == null -> ""
        profile.registeredAs == "Administrator" -> "[Administrator]"
        profile.registeredAs == "Expert" && profile.verified == true -> "[Expert]"
        profile.registeredAs == "Farmer" -> "Farmer"
        else -> ""
    }
}