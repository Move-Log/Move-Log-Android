package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.BasicResponse
import com.ilgusu.data.model.OnlyMsgDTO
import com.ilgusu.data.model.news.CreateNewsHeadLineRequestDTO
import com.ilgusu.data.model.news.CreateNewsHeadLineResponseDTO
import com.ilgusu.data.model.news.GetDateNewsRecordResponseDTO
import com.ilgusu.data.model.news.GetTodayNewsRecordResponseDTO
import com.ilgusu.data.model.news.GetWeekNewsRecordResponseDTO
import com.ilgusu.data.model.news.RecommendNewsKeywordResponseDTO
import com.ilgusu.data.service.NewsService
import com.ilgusu.domain.repository.TokenRepository
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(
    private val service: NewsService,
    private val tokenRepository: TokenRepository
): NewsRemoteDataSource {

    private suspend fun getAccessTokenWithPrefix(): String = "Bearer ${tokenRepository.getTokens().first().accessToken}"

    override suspend fun postNews(
        keywordId: Int,
        file: File,
        headLine: String
    ): Response<BasicResponse<OnlyMsgDTO>> {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("img", file.name, requestFile)

        val createRecordReqJson = """
            {
                "headLine": "$headLine"
            }
        """
        val createRecordReq = createRecordReqJson.toRequestBody("application/json".toMediaTypeOrNull())

        return service.postNews(getAccessTokenWithPrefix(), keywordId, body, createRecordReq)
    }

    override suspend fun createNewsHeadlines(
        keywordId: Int,
        body: CreateNewsHeadLineRequestDTO
    ): Response<BasicResponse<List<CreateNewsHeadLineResponseDTO>>> {
        return service.createNewsHeadlines(getAccessTokenWithPrefix(), keywordId, body)
    }

    override suspend fun recommendNewsKeywords(): Response<BasicResponse<List<RecommendNewsKeywordResponseDTO>>> {
        return service.recommendNewsKeywords(getAccessTokenWithPrefix())
    }

    override suspend fun getTodayNewsRecords(): Response<BasicResponse<GetTodayNewsRecordResponseDTO>> {
        return service.getTodayNewsRecords(getAccessTokenWithPrefix())
    }

    override suspend fun getWeekNewsRecords(page: Int): Response<BasicResponse<GetWeekNewsRecordResponseDTO>> {
        return service.getWeekNewsRecords(getAccessTokenWithPrefix(), page)
    }

    override suspend fun getDateNewsRecords(
        date: String,
        page: Int
    ): Response<BasicResponse<GetDateNewsRecordResponseDTO>> {
        return service.getDateNewsRecords(getAccessTokenWithPrefix(), date, page)
    }
}