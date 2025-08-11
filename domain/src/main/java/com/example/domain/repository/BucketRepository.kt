package com.example.domain.repository

interface BucketRepository {
    suspend fun upload(remotePath: String, data: ByteArray)
    suspend fun getImage(remotePath: String): ByteArray
}