package com.ilgusu.domain.usecase.auth

import com.ilgusu.domain.model.TokenPreferences
import com.ilgusu.domain.repository.TokenRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(refreshToken: String): Result<TokenPreferences> {
        return tokenRepository.refreshToken(refreshToken)
    }
}