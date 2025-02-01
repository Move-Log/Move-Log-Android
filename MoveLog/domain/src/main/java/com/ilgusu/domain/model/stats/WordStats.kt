package com.ilgusu.domain.model.stats

data class WordStats(
    val avgDailyRecord: Double,
    val avgWeeklyRecord: Double,
    val count: Int,
    val lastRecordedAt: String,
    val noun: String
)
