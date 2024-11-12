package com.ilgusu.domain.usecase.auth

import com.ilgusu.domain.repository.TokenRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    operator fun invoke() = tokenRepository.getTokens()
}
