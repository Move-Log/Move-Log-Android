package com.ilgusu.data.service

import com.ilgusu.data.model.BasicResponse
import com.ilgusu.data.model.OnlyMsgDTO
import com.ilgusu.data.model.news.GetRecentRecordImageDTO
import com.ilgusu.data.model.news.SearchRecordDTO
import com.ilgusu.data.model.record.CurrentImageUrlDTO
import com.ilgusu.data.model.record.TodayRecordResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RecordService {

    @Multipart
    @POST("/api/v1/record")
    suspend fun record(
        @Header("Authorization") accessToken: String,
        @Part file: MultipartBody.Part?,
        @Part("createRecordReq") createRecordReq: RequestBody
    ): Response<BasicResponse<OnlyMsgDTO>>

    @GET("/api/v1/record/today")
    suspend fun getTodayRecord(
        @Header("Authorization") accessToken: String
    ): Response<BasicResponse<TodayRecordResponseDTO>>

    @GET("/api/v1/record/search")
    suspend fun searchRecord(
        @Header("Authorization") accessToken: String,
    ): Response<BasicResponse<List<SearchRecordDTO>>>

    @GET("/api/v1/record/image/{keywordId}")
    suspend fun getRecentRecordImages(
        @Header("Authorization") accessToken: String,
        @Path("keywordId") keywordId: Int
    ): Response<BasicResponse<List<GetRecentRecordImageDTO>>>

    @GET("/api/v1/record/current")
    suspend fun getRecentCurrentImages(
        @Header("Authorization") accessToken: String
    ): Response<BasicResponse<List<CurrentImageUrlDTO>>>
}