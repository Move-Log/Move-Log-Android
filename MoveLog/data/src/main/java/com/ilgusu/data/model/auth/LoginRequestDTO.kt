package com.ilgusu.data.model.auth

import com.google.gson.annotations.SerializedName

data class LoginRequestDTO(
    @SerializedName("idToken")
    val idToken: String,
    @SerializedName("provider")
    val provider: String
)
