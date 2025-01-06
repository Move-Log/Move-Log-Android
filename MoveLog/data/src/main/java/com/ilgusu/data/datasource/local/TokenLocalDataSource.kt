package com.ilgusu.data.datasource.local

import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.domain.model.TokenPreferences
import kotlinx.coroutines.flow.Flow

interface TokenLocalDataSource {
    suspend fun saveTokens(authProvider: AuthProvider, accessToken: String, refreshToken: String): Result<Unit>

    fun getTokens(): Flow<TokenPreferences>

    suspend fun clearTokens(): Result<Unit>
}