package com.ilgusu.data.model.stats


import com.google.gson.annotations.SerializedName

data class TopRecord(
    @SerializedName("count")
    val count: Int,
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("trend")
    val trend: String
)