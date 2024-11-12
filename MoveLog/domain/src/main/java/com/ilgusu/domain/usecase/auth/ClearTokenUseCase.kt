package com.ilgusu.domain.usecase.auth

import com.ilgusu.domain.repository.TokenRepository
import javax.inject.Inject

class ClearTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return tokenRepository.clearTokens()
    }
}