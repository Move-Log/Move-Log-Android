package com.ilgusu.domain.model.stats

data class AllRecordStats(
    val avgDailyRecord: Double,
    val category: String,
    val maxConsecutiveDays: Int,
    val maxDailyRecord: Int,
    val topRecords: List<TopRecord>,
    val totalRecords: Int
)