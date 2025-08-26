package com.example.data.remote.services

import com.example.domain.model.ml.voca.MLVocaRequest
import com.example.domain.model.ml.voca.MLVocaResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

class MLVocaApiService @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    companion object {
        private const val BASE_URL = "https://mlvoca.com/"
        private val JSON_MEDIA_TYPE = "application/json".toMediaType()
    }

    suspend fun generateResponseStream(request: MLVocaRequest): Result<String> {
        return try {
            val jsonBody = """
                {
                    "model": "${request.model}",
                    "prompt": "${escapeJsonString(request.prompt)}"
                    ${request.max_tokens?.let { """, "max_tokens": $it""" } ?: ""}
                    ${request.temperature?.let { """, "temperature": $it""" } ?: ""}
                }
            """.trimIndent()

            val requestBuilder = Request.Builder()
                .url("${BASE_URL}api/generate")
                .post(jsonBody.toRequestBody(JSON_MEDIA_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")

            val response = okHttpClient.newCall(requestBuilder.build()).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""
                val cleanResponse = cleanUpResponse(responseBody)
                Result.success(cleanResponse)
            } else {
                Result.failure(Exception("API error: ${response.code} - ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun cleanUpResponse(responseBody: String): String {
        val fullResponse = StringBuilder()
        var isFinalResponse = false
        var isFirstChunk = true

        responseBody.lines()
            .filter { it.isNotBlank() && it.isNotEmpty() }
            .forEach { line ->
                try {
                    val jsonObject = JSONObject(line)
                    if (jsonObject.has("response")) {
                        var responseChunk = jsonObject.getString("response")

                        responseChunk = unescapeUnicode(responseChunk)

                        responseChunk = responseChunk.replace(Regex("\\*\\*Q:.*?\\*\\*\\s*\\*\\*A:\\*\\*\\s*"), "")

                        responseChunk = responseChunk.replace(Regex("\\*\\*"), "")

                        if (responseChunk.contains("</think>")) {
                            isFinalResponse = true
                            responseChunk = responseChunk.substringAfter("</think>")
                            responseChunk = responseChunk.replace(Regex("^\\n+"), "")
                        }

                        if (isFinalResponse && responseChunk.isNotBlank()) {
                            if (isFirstChunk) {
                                fullResponse.append(responseChunk.trimStart())
                                isFirstChunk = false
                            } else {
                                fullResponse.append(responseChunk)
                            }
                        }

                        if (jsonObject.optBoolean("done", false)) {
                        }
                    }
                } catch (e: Exception) {
                }
            }

        return fullResponse.toString()
            .replace("\u003cthink\u003e", "")
            .replace("\u003c/think\u003e", "")
            .replace(Regex("\\*\\*Q:.*?\\*\\*\\s*\\*\\*A:\\*\\*\\s*"), "")
            .replace(Regex("\\*\\*"), "")
            .replace(Regex("\\s+\\n"), "\n")
            .replace(Regex("\\n+"), "\n")
            .replace(Regex("^\\s+"), "")
            .replace(Regex("\\s+$"), "")
            .trim()
    }

    private fun unescapeUnicode(input: String): String {
        return input.replace("\\u003c", "<")
            .replace("\\u003e", ">")
            .replace("\\u0026", "&")
            .replace("\\u0027", "'")
            .replace("\\u0022", "\"")
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\t", "\t")
    }

    private fun escapeJsonString(input: String): String {
        return input.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}