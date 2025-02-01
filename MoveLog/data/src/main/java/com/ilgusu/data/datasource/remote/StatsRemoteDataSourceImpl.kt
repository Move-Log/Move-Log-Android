package com.ilgusu.data.datasource.remote

import com.ilgusu.data.model.stats.WordIdStatsResponseDTO
import com.ilgusu.data.model.stats.WordStatsResponseDTO
import com.ilgusu.data.service.StatsService
import com.ilgusu.domain.repository.TokenRepository
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

class StatsRemoteDataSourceImpl @Inject constructor(
    private val service: StatsService,
    private val tokenRepository: TokenRepository
) : StatsRemoteDataSource {

    private suspend fun getAccessTokenWithPrefix(): String =
        "Bearer ${tokenRepository.getTokens().first().accessToken}"

    override suspend fun searchWords(keyword: String): Response<List<WordIdStatsResponseDTO>> {
        return service.searchWords(getAccessTokenWithPrefix(), keyword)
    }

    override suspend fun getRecentRecordWord(): Response<List<WordIdStatsResponseDTO>> {
        return service.getRecentRecordWord(getAccessTokenWithPrefix())
    }

    override suspend fun myWordStats(): Response<WordStatsResponseDTO> {
        return service.myWordStats(getAccessTokenWithPrefix())
    }

    override suspend fun allWordStats(): Response<WordStatsResponseDTO> {
        return service.allWordStats(getAccessTokenWithPrefix())
    }
}