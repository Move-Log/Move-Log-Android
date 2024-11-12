package com.ilgusu.data.repository

import com.ilgusu.data.datasource.local.TokenLocalDataSource
import com.ilgusu.data.datasource.remote.AuthRemoteDataSource
import com.ilgusu.data.mapper.toDomain
import com.ilgusu.domain.model.TokenPreferences
import com.ilgusu.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : TokenRepository {
    override suspend fun saveTokens(accessToken: String, refreshToken: String) : Result<Unit> {
        return tokenLocalDataSource.saveTokens(accessToken, refreshToken)
    }

    override fun getTokens() = tokenLocalDataSource.getTokens()

    override suspend fun clearTokens(): Result<Unit> {
        return tokenLocalDataSource.clearTokens()
    }

    override suspend fun refreshToken(refreshToken: String): Result<TokenPreferences> {
        return try {
            val response = authRemoteDataSource.refreshToken(refreshToken)
            Result.success(response.toDomain)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}