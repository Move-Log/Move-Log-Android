package com.ilgusu.data.model.news

import com.google.gson.annotations.SerializedName

data class NewsContentDTO(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("headLine")
    val headLine: String,
    @SerializedName("newsId")
    val newsId: Int,
    @SerializedName("newsImageUrl")
    val newsImageUrl: String,
    @SerializedName("noun")
    val noun: String,
    @SerializedName("verb")
    val verb: String
)
