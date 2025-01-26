package com.ilgusu.domain.usecase.auth

import com.ilgusu.domain.repository.AuthRepository
import javax.inject.Inject

class SocialWithdrawUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return authRepository.socialWithdraw()
    }
}