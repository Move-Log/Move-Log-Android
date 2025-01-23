package com.ilgusu.data.model.news

import com.google.gson.annotations.SerializedName

data class CreateNewsHeadLineRequestDTO(
    @SerializedName("option")
    val option: String
)
