package com.ilgusu.domain.model

data class TokenPreferences(
    val accessToken: String = "",
    val refreshToken: String = "",
)