package com.ilgusu.data.repository

import com.ilgusu.data.service.KakaoAuthService
import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val kakaoAuthService: KakaoAuthService
) : AuthRepository {

    override suspend fun login(provider: AuthProvider): Result<String> {
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

    override suspend fun signOut(provider: AuthProvider): Result<Boolean> {
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

    override suspend fun withdraw(provider: AuthProvider): Result<Boolean> {
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
}