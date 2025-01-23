package com.ilgusu.data.model.news

import com.google.gson.annotations.SerializedName

data class CreateNewsHeadLineResponseDTO(
    @SerializedName("headLine")
    val headline: String
)
