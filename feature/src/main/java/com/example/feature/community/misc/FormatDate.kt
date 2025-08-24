package com.example.feature.community.misc

fun formatDate(dateString: String): String {
    return try {
        val parts = dateString.split("T")[0].split("-")
        "${parts[1]}/${parts[2]}/${parts[0]}"
    } catch (e: Exception) {
        dateString
    }
}