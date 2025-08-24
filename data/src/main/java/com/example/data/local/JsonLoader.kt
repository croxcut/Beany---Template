// ===============================================================================
//
// Copyright (C) 2025-2026 by John Paul Valenzuela
//
// This source is available for distribution and/or modification
// only under the terms of the Beany Source Code License as
// published by Beany. All rights reserved.
//
// The source is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// FITNESS FOR A PARTICULAR PURPOSE. See the Beany Source Code License
// for more details.
//
// ===============================================================================

package com.example.data.local

import android.content.Context
import com.example.domain.model.weather.City
import com.example.domain.model.Terms
import com.google.gson.Gson
import kotlinx.serialization.json.*
import java.io.InputStreamReader

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

fun loadTermsFromAssets(context: Context, fileName: String = "beany_terms.json"): Terms {
    val inputStream = context.assets.open(fileName)
    val reader = InputStreamReader(inputStream)
    return Gson().fromJson(reader, Terms::class.java)
}