package com.ilgusu.data.model.auth

import com.google.gson.annotations.SerializedName

data class LoginResponseDTO(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("isRegistered")
    val isRegistered: Boolean,
    @SerializedName("role")
    val role: String,
    @SerializedName("tokenType")
    val tokenType: String
)