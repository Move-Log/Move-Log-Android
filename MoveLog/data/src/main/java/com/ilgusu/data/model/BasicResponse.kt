package com.ilgusu.data.model

import com.google.gson.annotations.SerializedName

data class BasicResponse<T>(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: T
)
