package com.ilgusu.data.model.stats


import com.google.gson.annotations.SerializedName

data class WordStatsResponseDTO(
    @SerializedName("avgDailyRecord")
    val avgDailyRecord: Double,
    @SerializedName("avgWeeklyRecord")
    val avgWeeklyRecord: Double,
    @SerializedName("count")
    val count: Int,
    @SerializedName("lastRecordedAt")
    val lastRecordedAt: String,
    @SerializedName("noun")
    val noun: String
)