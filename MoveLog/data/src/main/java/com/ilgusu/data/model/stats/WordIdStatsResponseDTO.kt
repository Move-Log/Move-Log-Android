package com.ilgusu.data.model.stats


import com.google.gson.annotations.SerializedName

data class WordIdStatsResponseDTO(
    @SerializedName("keywordId")
    val keywordId: Int,
    @SerializedName("noun")
    val noun: String
)