package com.ilgusu.data.model.stats


import com.google.gson.annotations.SerializedName

data class GetAllRecordStatsResponseDTO(
    @SerializedName("avgDailyRecord")
    val avgDailyRecord: Double,
    @SerializedName("category")
    val category: String,
    @SerializedName("maxConsecutiveDays")
    val maxConsecutiveDays: Int,
    @SerializedName("maxDailyRecord")
    val maxDailyRecord: Int,
    @SerializedName("topRecords")
    val topRecords: List<TopRecord>,
    @SerializedName("totalRecords")
    val totalRecords: Int
)