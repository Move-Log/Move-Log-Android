package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.BasicResponse
import com.ilgusu.data.model.OnlyMsgDTO
import com.ilgusu.data.model.news.GetRecentRecordImageDTO
import com.ilgusu.data.model.news.SearchRecordDTO
import com.ilgusu.data.model.record.GetDateRecordCalendarResponseDTO
import com.ilgusu.data.model.record.TodayRecordResponseDTO
import retrofit2.Response
import java.io.File

interface RecordRemoteDataSource {

    suspend fun record(
        file: File?,
        type: String,
        word: String
    ): Response<BasicResponse<OnlyMsgDTO>>

    suspend fun getTodayRecord(): Response<BasicResponse<TodayRecordResponseDTO>>

    suspend fun searchRecord(): Response<BasicResponse<List<SearchRecordDTO>>>

    suspend fun getRecentRecordImages(keywordId: Int): Response<BasicResponse<List<GetRecentRecordImageDTO>>>

    suspend fun getCalendarRecords(
        date: String,
        page: Int = 0
    ): Response<BasicResponse<GetDateRecordCalendarResponseDTO>>
}