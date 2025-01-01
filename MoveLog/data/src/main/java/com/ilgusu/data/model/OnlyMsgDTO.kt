package com.ilgusu.data.model

import com.google.gson.annotations.SerializedName

data class OnlyMsgDTO(
    @SerializedName("message")
    val message: String
)
