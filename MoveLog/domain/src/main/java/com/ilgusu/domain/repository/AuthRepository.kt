package com.ilgusu.domain.repository

import com.ilgusu.domain.model.AuthProvider

interface AuthRepository {

    suspend fun socialLogin(provider: AuthProvider): Result<String>

    suspend fun socialSignOut(provider: AuthProvider): Result<Boolean>

    suspend fun socialWithdraw(provider: AuthProvider): Result<Boolean>

    suspend fun login(idToken: String, provider: AuthProvider): Result<Boolean>

    suspend fun signUp(): Result<Boolean>

    suspend fun withdraw(): Result<Boolean>
}