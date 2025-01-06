package com.ilgusu.domain.model

data class TokenPreferences(
    val provider: AuthProvider,
    val accessToken: String = "",
    val refreshToken: String = "",
)