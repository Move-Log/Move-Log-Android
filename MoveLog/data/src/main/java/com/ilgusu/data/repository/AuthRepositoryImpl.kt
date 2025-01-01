package com.ilgusu.data.repository

import com.ilgusu.data.datasource.remote.AuthRemoteDataSource
import com.ilgusu.data.service.KakaoAuthService
import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.domain.repository.AuthRepository
import com.ilgusu.domain.repository.TokenRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val kakaoAuthService: KakaoAuthService,
    private val dataSource: AuthRemoteDataSource,
    private val tokenRepository: TokenRepository
) : AuthRepository {

    override suspend fun socialLogin(provider: AuthProvider): Result<String> {
        return try {
            when (provider) {
                AuthProvider.KAKAO -> {
                    val idToken = kakaoAuthService.signInWithKakao()
                    Result.success(idToken)
                }
                AuthProvider.GOOGLE -> {
                    Result.success("")
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun socialSignOut(provider: AuthProvider): Result<Boolean> {
        return try {
            when (provider) {
                AuthProvider.KAKAO -> {
                    kakaoAuthService.signOut()
                    Result.success(true)
                }
                AuthProvider.GOOGLE -> {
                    Result.success(true)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun socialWithdraw(provider: AuthProvider): Result<Boolean> {
        return try {
            when (provider) {
                AuthProvider.KAKAO -> {
                    kakaoAuthService.withdraw()
                    Result.success(true)
                }
                AuthProvider.GOOGLE -> {
                    Result.success(true)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(idToken: String, provider: AuthProvider): Result<Boolean> {
        return try {
            val response = dataSource.login(idToken, provider)
            if(response.isSuccessful) {
                val body = response.body()
                if(body != null) {
                    tokenRepository.saveTokens(body.accessToken, "")
                    Result.success(body.isRegistered)
                } else {
                    throw Exception("Body is null")
                }
            } else {
                throw Exception("Request is failure")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun withdraw(): Result<Boolean> {
        return try {
            val response = dataSource.withdraw()
            if(response.isSuccessful) {
                val body = response.body()
                if(body != null) {
                    tokenRepository.clearTokens()
                    Result.success(true)
                } else {
                    throw Exception("Body is null")
                }
            } else {
                throw Exception("Request is failure")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}