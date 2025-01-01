package com.ilgusu.domain.repository

import com.ilgusu.domain.model.AuthProvider

interface AuthRepository {

    suspend fun login(provider: AuthProvider): Result<String>

    suspend fun signOut(provider: AuthProvider): Result<Boolean>

    suspend fun withdraw(provider: AuthProvider): Result<Boolean>
}