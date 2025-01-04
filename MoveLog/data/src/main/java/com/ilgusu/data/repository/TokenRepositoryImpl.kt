package com.ilgusu.data.repository

import com.ilgusu.data.datasource.local.TokenLocalDataSource
import com.ilgusu.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource
) : TokenRepository {
    override suspend fun saveTokens(accessToken: String, refreshToken: String) : Result<Unit> {
        return tokenLocalDataSource.saveTokens(accessToken, refreshToken)
    }

    override fun getTokens() = tokenLocalDataSource.getTokens()

    override suspend fun clearTokens(): Result<Unit> {
        return tokenLocalDataSource.clearTokens()
    }
}