package com.ilgusu.data.model.news

import com.google.gson.annotations.SerializedName

data class GetRecentRecordImageDTO(
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("createdAt")
    val createAt: String
)
