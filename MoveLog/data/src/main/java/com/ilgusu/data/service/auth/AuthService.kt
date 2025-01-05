package com.ilgusu.data.service.auth

import com.ilgusu.data.model.OnlyMsgDTO
import com.ilgusu.data.model.auth.LoginRequestDTO
import com.ilgusu.data.model.auth.LoginResponseDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/login")
    suspend fun login(
        @Body body: LoginRequestDTO
    ): Response<LoginResponseDTO>

    @DELETE("/auth")
    suspend fun withdraw(
        @Header("Authorization") accessToken: String
    ): Response<OnlyMsgDTO>

    @POST("/auth/sign-up")
    suspend fun signUp(
        @Header("Authorization") accessToken: String
    ): Response<ResponseBody>
}