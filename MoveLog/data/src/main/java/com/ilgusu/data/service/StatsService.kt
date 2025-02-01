package com.ilgusu.data.service

import com.ilgusu.data.model.stats.WordIdStatsResponseDTO
import com.ilgusu.data.model.stats.WordStatsResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StatsService {

    @GET("/api/v1/stats/word/search")
    suspend fun searchWords(
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String
    ): Response<List<WordIdStatsResponseDTO>>

    @GET("/api/v1/stats/word/recommend")
    suspend fun getRecentRecordWord(
        @Header("Authorization") accessToken: String
    ): Response<List<WordIdStatsResponseDTO>>

    @GET("/api/v1/stats/word/my/{keywordId}")
    suspend fun myWordStats(
        @Header("Authorization") accessToken: String
    ): Response<WordStatsResponseDTO>

    @GET("/api/v1/stats/word/all")
    suspend fun allWordStats(
        @Header("Authorization") accessToken: String
    ): Response<WordStatsResponseDTO>
}