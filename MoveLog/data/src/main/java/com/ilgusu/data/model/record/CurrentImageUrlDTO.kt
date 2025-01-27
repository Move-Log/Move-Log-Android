package com.ilgusu.data.model.record

import com.google.gson.annotations.SerializedName

data class CurrentImageUrlDTO(
    @SerializedName("imageUrl")
    val imageUrl: String
)
