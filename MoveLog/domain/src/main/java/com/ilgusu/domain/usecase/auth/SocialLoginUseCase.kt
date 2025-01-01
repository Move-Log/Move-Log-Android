package com.ilgusu.domain.usecase.auth

import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.domain.repository.AuthRepository
import javax.inject.Inject

class SocialLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(provider: AuthProvider): Result<String> {
        return authRepository.login(provider)
    }
}