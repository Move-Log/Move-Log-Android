package com.ilgusu.data.service

import com.ilgusu.data.model.BasicResponse
import com.ilgusu.data.model.OnlyMsgDTO
import com.ilgusu.data.model.record.TodayRecordResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
}