package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.TokenResponse
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
//    private val service: AuthService
): AuthRemoteDataSource {

    override suspend fun refreshToken(refreshToken: String): TokenResponse {
//        return authApi.refreshToken(refreshToken)
        return TokenResponse("test_access_token", "test_refresh_token")
    }
}