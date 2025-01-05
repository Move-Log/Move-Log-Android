package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.BasicResponse
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

    override suspend fun record(
        file: File?,
        type: Int,
        word: String
    ): Response<BasicResponse<String>> {
        val tokens = tokenRepository.getTokens().first()

        val requestFile = if(file != null) RequestBody.create("image/jpeg".toMediaTypeOrNull(), file) else null
        val body = if (requestFile != null)MultipartBody.Part.createFormData("img", file?.name, requestFile) else null

        val createRecordReqJson = """
            {
                "verbType":$type,
                "noun":"$word"
            }
        """
        val createRecordReq = RequestBody.create("application/json".toMediaTypeOrNull(), createRecordReqJson)

        return service.record("Bearer " + tokens.accessToken, body, createRecordReq)
    }

    override suspend fun getTodayRecord(): Response<BasicResponse<List<Int>>> {
        val tokens = tokenRepository.getTokens().first()
        return service.getTodayRecord("Bearer " + tokens.accessToken)
    }
}