package com.ilgusu.domain.repository

import com.ilgusu.domain.model.stats.WordIdStats
import com.ilgusu.domain.model.stats.WordStats

interface StatsRepository {

    suspend fun searchWords(keyword: String): Result<List<WordIdStats>>

    suspend fun getRecentRecordWord(): Result<List<WordIdStats>>

    suspend fun myWordStats(keywordId: Int): Result<WordStats>

    suspend fun allWordStats(keyword: String): Result<WordStats>
}