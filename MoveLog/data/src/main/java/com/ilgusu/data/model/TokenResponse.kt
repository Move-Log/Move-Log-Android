package com.ilgusu.data.model

import com.ilgusu.domain.model.AuthProvider

data class TokenResponse(
    val authProvider: AuthProvider,
    val accessToken: String,
    val refreshToken: String
)