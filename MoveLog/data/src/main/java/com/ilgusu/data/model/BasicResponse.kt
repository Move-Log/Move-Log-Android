package com.ilgusu.data.model

import com.google.gson.annotations.SerializedName

data class BasicResponse<T>(
    @SerializedName("body")
    val body: BasicBody<T>,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusCodeValue")
    val statusCodeValue: Int
)

data class BasicBody<T>(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: T
)
