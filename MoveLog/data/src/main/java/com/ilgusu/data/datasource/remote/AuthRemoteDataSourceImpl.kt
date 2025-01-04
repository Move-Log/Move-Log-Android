package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.OnlyMsgDTO
import com.ilgusu.data.model.TokenResponse
import com.ilgusu.data.model.auth.LoginRequestDTO
import com.ilgusu.data.model.auth.LoginResponseDTO
import com.ilgusu.data.service.auth.AuthService
import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.domain.repository.TokenRepository
import kotlinx.coroutines.flow.first
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val service: AuthService,
    private val tokenRepository: TokenRepository
): AuthRemoteDataSource {

    override suspend fun login(idToken: String, provider: AuthProvider): Response<LoginResponseDTO> {
        return service.login(
            LoginRequestDTO(idToken, provider.name.lowercase())
        )
    }

    override suspend fun withdraw(): Response<OnlyMsgDTO> {
        val tokens = tokenRepository.getTokens().first()
        return service.withdraw("Bearer " + tokens.accessToken)
    }

    override suspend fun signUp(): Response<ResponseBody> {
        val tokens = tokenRepository.getTokens().first()
        return service.signUp("Bearer " + tokens.accessToken)
    }

    override suspend fun refreshToken(refreshToken: String): TokenResponse {
//        return authApi.refreshToken(refreshToken)
        return TokenResponse("test_access_token", "test_refresh_token")
    }
}