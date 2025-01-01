package com.ilgusu.domain.usecase.auth

import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(idToken: String, provider: AuthProvider): Result<Boolean> {
        return authRepository.login(idToken, provider)
    }
}