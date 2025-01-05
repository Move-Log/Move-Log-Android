package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.BasicResponse
import retrofit2.Response
import java.io.File

interface RecordRemoteDataSource {

    suspend fun record(
        file: File?,
        type: Int,
        word: String
    ): Response<BasicResponse<String>>

    suspend fun getTodayRecord(): Response<BasicResponse<List<Int>>>
}