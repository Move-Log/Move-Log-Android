package com.ilgusu.domain.usecase.auth

import com.ilgusu.domain.repository.TokenRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(accessToken: String, refreshToken: String, ): Result<Unit> {
        return tokenRepository.saveTokens(accessToken, refreshToken)
    }
}