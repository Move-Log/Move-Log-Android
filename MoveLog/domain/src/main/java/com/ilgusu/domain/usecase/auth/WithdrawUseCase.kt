package com.ilgusu.domain.usecase.auth

import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.domain.repository.AuthRepository
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<Boolean> {
        return authRepository.withdraw()
    }
}