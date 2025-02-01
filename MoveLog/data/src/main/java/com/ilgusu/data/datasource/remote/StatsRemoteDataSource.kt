package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.stats.WordIdStatsResponseDTO
import com.ilgusu.data.model.stats.WordStatsResponseDTO
import retrofit2.Response

interface StatsRemoteDataSource {

    suspend fun searchWords(keyword: String): Response<List<WordIdStatsResponseDTO>>

    suspend fun getRecentRecordWord(): Response<List<WordIdStatsResponseDTO>>

    suspend fun myWordStats(): Response<WordStatsResponseDTO>

    suspend fun allWordStats(): Response<WordStatsResponseDTO>
}