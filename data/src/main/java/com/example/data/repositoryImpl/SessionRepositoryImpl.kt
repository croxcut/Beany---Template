package com.example.data.repositoryImpl

import android.util.Log
import com.example.domain.repository.SessionRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
): SessionRepository {

    private val _currentSession = MutableStateFlow<UserSession?>(null)
    val currentSession = _currentSession.asStateFlow()

    init {
        _currentSession.value = supabaseClient.auth.currentSessionOrNull()
        Log.i("SessionManager", "Initial session: ${_currentSession.value}")
    }

    override suspend fun getCurrentSession(): Result<UserSession> {
        return try {
            val session = _currentSession.value?: throw Exception("Failed to load session")
            Result.success(session)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCurrentSession(): Result<UserSession?> {
        return try {
            supabaseClient.auth.refreshCurrentSession()
            val session = supabaseClient.auth.currentSessionOrNull()
                ?: throw Exception("Failed to refresh Current Session")

            _currentSession.value = session
            Result.success(session)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearCurrentSession(): Result<Unit> {
        return try {
            supabaseClient.auth.signOut()
            _currentSession.value = null
            Log.i("SessionManager", "Logged out")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SessionManager", "Logout failed", e)
            Result.failure(e)
        }
    }

    fun isSignedUp(): Boolean {
        return _currentSession.value != null
    }

}