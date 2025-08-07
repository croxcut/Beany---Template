package com.example.data.local

import android.content.Context
import com.example.domain.model.City
import kotlinx.serialization.json.*
import java.io.File

fun loadCity(context: Context, fileName: String): List<City> {
    val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val json = Json.parseToJsonElement(jsonString).jsonObject
    val citiesJsonArray = json["cities"]?.jsonArray ?: return emptyList()

    return citiesJsonArray.mapIndexed { index, jsonElement ->
        val obj = jsonElement.jsonObject
        City(
            id = index + 1,
            name = obj["city-name"]?.jsonPrimitive?.content?.replaceFirstChar { it.uppercase() } ?: "Unknown",
            latitude = obj["latitude"]?.jsonPrimitive?.double ?: 0.0,
            longitude = obj["longitude"]?.jsonPrimitive?.double ?: 0.0
        )
    }
}