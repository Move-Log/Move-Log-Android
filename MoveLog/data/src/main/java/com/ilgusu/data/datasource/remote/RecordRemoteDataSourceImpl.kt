package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.BasicResponse
import com.ilgusu.data.model.OnlyMsgDTO
import com.ilgusu.data.model.news.GetRecentRecordImageDTO
import com.ilgusu.data.model.news.SearchRecordDTO
import com.ilgusu.data.model.record.CurrentImageUrlDTO
import com.ilgusu.data.model.record.GetDateRecordCalendarResponseDTO
import com.ilgusu.data.model.record.TodayRecordResponseDTO
import com.ilgusu.data.service.RecordService
import com.ilgusu.domain.repository.TokenRepository
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class RecordRemoteDataSourceImpl @Inject constructor(
    private val service: RecordService,
    private val tokenRepository: TokenRepository
): RecordRemoteDataSource {

    private suspend fun getAccessTokenWithPrefix(): String = "Bearer ${tokenRepository.getTokens().first().accessToken}"

    override suspend fun record(
        file: File?,
        type: String,
        word: String
    ): Response<BasicResponse<OnlyMsgDTO>> {
        val requestFile = if(file != null) RequestBody.create("image/jpeg".toMediaTypeOrNull(), file) else null
        val body = if (requestFile != null)MultipartBody.Part.createFormData("img", file?.name, requestFile) else null

        val createRecordReqJson = """
            {
                "verbType":"$type",
                "noun":"$word"
            }
        """
        val createRecordReq = RequestBody.create("application/json".toMediaTypeOrNull(), createRecordReqJson)

        return service.record(getAccessTokenWithPrefix(), body, createRecordReq)
    }

    override suspend fun getTodayRecord(): Response<BasicResponse<TodayRecordResponseDTO>> {
        return service.getTodayRecord(getAccessTokenWithPrefix())
    }

    override suspend fun searchRecord(): Response<BasicResponse<List<SearchRecordDTO>>> {
        return service.searchRecord(getAccessTokenWithPrefix())
    }

    override suspend fun getRecentRecordImages(keywordId: Int): Response<BasicResponse<List<GetRecentRecordImageDTO>>> {
        return service.getRecentRecordImages(getAccessTokenWithPrefix(), keywordId)
    }

    override suspend fun getRecentCurrentImages(): Response<BasicResponse<List<CurrentImageUrlDTO>>> {
        return service.getRecentCurrentImages(getAccessTokenWithPrefix())
    }

    override suspend fun getCalendarRecords(
        date: String,
        page: Int
    ): Response<BasicResponse<GetDateRecordCalendarResponseDTO>> {
        return service.getCalendarRecords(getAccessTokenWithPrefix(), date, page)
    }
}