package com.ilgusu.data.service

import com.ilgusu.data.model.BasicResponse
import com.ilgusu.data.model.OnlyMsgDTO
import com.ilgusu.data.model.news.CreateNewsHeadLineRequestDTO
import com.ilgusu.data.model.news.CreateNewsHeadLineResponseDTO
import com.ilgusu.data.model.news.GetDateNewsRecordResponseDTO
import com.ilgusu.data.model.news.GetTodayNewsRecordResponseDTO
import com.ilgusu.data.model.news.GetWeekNewsRecordResponseDTO
import com.ilgusu.data.model.news.RecommendNewsKeywordResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsService {

    @Multipart
    @POST("/api/v1/news/{keywordId}")
    suspend fun postNews(
        @Header("Authorization") accessToken: String,
        @Path("keywordId") keywordId: Int,
        @Part file: MultipartBody.Part?,
        @Part("createNewsReq") createRecordReq: RequestBody
    ): Response<BasicResponse<OnlyMsgDTO>>

    @POST("/api/v1/news/{keywordId}/headline")
    suspend fun createNewsHeadlines(
        @Header("Authorization") accessToken: String,
        @Path("keywordId") keywordId: Int,
        @Body body: CreateNewsHeadLineRequestDTO
    ): Response<BasicResponse<List<CreateNewsHeadLineResponseDTO>>>

    @GET("/api/v1/news/recommend")
    suspend fun recommendNewsKeywords(
        @Header("Authorization") accessToken: String
    ): Response<BasicResponse<List<RecommendNewsKeywordResponseDTO>>>

    @GET("/api/v1/news/today")
    suspend fun getTodayNewsRecords(
        @Header("Authorization") accessToken: String
    ): Response<BasicResponse<GetTodayNewsRecordResponseDTO>>

    @GET("/api/v1/news/week")
    suspend fun getWeekNewsRecords(
        @Header("Authorization") accessToken: String,
        @Query("page") page: Int = 0
    ): Response<BasicResponse<GetWeekNewsRecordResponseDTO>>

    @GET("/api/v1/news/calendar/{date}")
    suspend fun getDateNewsRecords(
        @Header("Authorization") accessToken: String,
        @Path("date") date: String,
        @Query("page") page: Int = 0
    ): Response<BasicResponse<GetDateNewsRecordResponseDTO>>
}