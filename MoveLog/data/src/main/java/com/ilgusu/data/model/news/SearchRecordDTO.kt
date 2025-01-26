package com.ilgusu.data.model.news

import com.google.gson.annotations.SerializedName

data class SearchRecordDTO(
    @SerializedName("keywordId")
    val keywordId: Int,
    @SerializedName("noun")
    val noun: String,
    @SerializedName("verb")
    val verb: String
)
