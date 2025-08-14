package com.example.data.repositoryImpl

import com.example.domain.repository.BucketRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import javax.inject.Inject
import android.util.Log

class BucketRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : BucketRepository {

    private val TAG = "BucketRepositoryImpl"

    override suspend fun upload(remotePath: String, data: ByteArray) {
        val bucket = supabaseClient.storage.from("profilepictures")
        try {
            Log.d(TAG, "Attempting upload to path: $remotePath, size=${data.size} bytes")

            // This will overwrite any existing file
            bucket.upload(path = remotePath, data) {
                upsert = true
            }

            Log.d(TAG, "Upload successful!")
        } catch (e: Exception) {
            Log.e(TAG, "Upload failed!", e)
            // Do not rethrow if you want to prevent crashes
        }
    }

    override suspend fun getImage(remotePath: String): ByteArray {
        val bucket = supabaseClient.storage.from("profilepictures")
        return bucket.downloadAuthenticated(remotePath)
    }
}