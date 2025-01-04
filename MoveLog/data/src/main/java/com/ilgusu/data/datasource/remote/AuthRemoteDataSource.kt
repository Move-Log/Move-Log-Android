package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.OnlyMsgDTO
import com.ilgusu.data.model.TokenResponse
import com.ilgusu.data.model.auth.LoginResponseDTO
import com.ilgusu.domain.model.AuthProvider
import okhttp3.ResponseBody
import retrofit2.Response

interface AuthRemoteDataSource {

    suspend fun login(idToken: String, provider: AuthProvider): Response<LoginResponseDTO>

    suspend fun withdraw(): Response<OnlyMsgDTO>

    suspend fun signUp(): Response<ResponseBody>

    suspend fun refreshToken(refreshToken: String): TokenResponse
}