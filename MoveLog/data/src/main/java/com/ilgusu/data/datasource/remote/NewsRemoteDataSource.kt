package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.BasicResponse
import com.ilgusu.data.model.OnlyMsgDTO
import com.ilgusu.data.model.news.CreateNewsHeadLineRequestDTO
import com.ilgusu.data.model.news.CreateNewsHeadLineResponseDTO
import com.ilgusu.data.model.news.GetDateNewsRecordResponseDTO
import com.ilgusu.data.model.news.GetTodayNewsRecordResponseDTO
import com.ilgusu.data.model.news.GetWeekNewsRecordResponseDTO
import com.ilgusu.data.model.news.RecommendNewsKeywordResponseDTO
import retrofit2.Response
import java.io.File

interface NewsRemoteDataSource {

    suspend fun postNews(
        keywordId: Int,
        file: File,
        headLine: String
    ): Response<BasicResponse<OnlyMsgDTO>>

    suspend fun createNewsHeadlines(
        keywordId: Int,
        body: CreateNewsHeadLineRequestDTO
    ): Response<BasicResponse<List<CreateNewsHeadLineResponseDTO>>>

    suspend fun recommendNewsKeywords(): Response<BasicResponse<List<RecommendNewsKeywordResponseDTO>>>

    suspend fun getTodayNewsRecords(): Response<BasicResponse<GetTodayNewsRecordResponseDTO>>

    suspend fun getWeekNewsRecords(page: Int = 0): Response<BasicResponse<GetWeekNewsRecordResponseDTO>>

    suspend fun getDateNewsRecords(
        date: String,
        page: Int = 0
    ): Response<BasicResponse<GetDateNewsRecordResponseDTO>>
}