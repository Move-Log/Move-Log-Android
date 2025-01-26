package com.ilgusu.data.mapper

import com.ilgusu.data.model.TokenResponse
import com.ilgusu.domain.model.TokenPreferences

val TokenResponse.toDomain
    get() = TokenPreferences(this.authProvider, this.accessToken, this.refreshToken)