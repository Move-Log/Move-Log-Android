package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.stats.GetAllRecordStatsResponseDTO
import com.ilgusu.data.model.stats.WordIdStatsResponseDTO
import com.ilgusu.data.model.stats.WordStatsResponseDTO
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Query

interface StatsRemoteDataSource {

    suspend fun searchWords(keyword: String): Response<List<WordIdStatsResponseDTO>>

    suspend fun getRecentRecordWord(): Response<List<WordIdStatsResponseDTO>>

    suspend fun myWordStats(keywordId: Int): Response<WordStatsResponseDTO>

    suspend fun allWordStats(keyword: String): Response<WordStatsResponseDTO>

    suspend fun getAllRecordStats(
        category: String,
        period: String,
        month: String? = null,
    ): Response<GetAllRecordStatsResponseDTO>
}