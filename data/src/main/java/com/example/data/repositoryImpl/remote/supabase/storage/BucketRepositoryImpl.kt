package com.example.data.repositoryImpl.remote.supabase.storage

import android.util.Log
import com.example.domain.repository.remote.supabase.BucketRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

class BucketRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : BucketRepository {
    private val TAG = "BucketRepositoryImpl"

    override suspend fun upload(remotePath: String, data: ByteArray) {
        val bucket = supabaseClient.storage.from("profilepictures")
        try {
            Log.d(TAG, "Attempting upload to path: $remotePath, size=${data.size} bytes")

            bucket.upload(path = remotePath, data) {
                upsert = true
            }

            Log.d(TAG, "Upload successful!")
        } catch (e: Exception) {
            Log.e(TAG, "Upload failed!", e)
        }
    }

    override suspend fun getImage(remotePath: String): ByteArray {
        return try {
            val bucket = supabaseClient.storage.from("profilepictures")
            val imageUrl = bucket.createSignedUrl(
                path = remotePath,
                expiresIn = 3.minutes
            )

            withContext(Dispatchers.IO) {
                URL(imageUrl).openStream().use { inputStream ->
                    inputStream.readBytes()
                }
            }
        } catch (e: Exception) {
            throw IOException("Failed to load image from path: $remotePath", e)
        }
    }
}