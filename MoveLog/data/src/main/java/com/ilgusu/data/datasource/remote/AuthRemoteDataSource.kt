package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.TokenResponse

interface AuthRemoteDataSource {
    suspend fun refreshToken(refreshToken: String): TokenResponse
}