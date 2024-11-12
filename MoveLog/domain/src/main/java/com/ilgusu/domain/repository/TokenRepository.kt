package com.ilgusu.domain.repository

import com.ilgusu.domain.model.TokenPreferences
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit>

    fun getTokens(): Flow<TokenPreferences>

    suspend fun clearTokens(): Result<Unit>

    suspend fun refreshToken(refreshToken: String): Result<TokenPreferences>
}