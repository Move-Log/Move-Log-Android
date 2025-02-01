package com.ilgusu.data.repository

import com.ilgusu.data.datasource.remote.RecordRemoteDataSource
import com.ilgusu.data.datasource.remote.StatsRemoteDataSource
import com.ilgusu.domain.model.Pageable
import com.ilgusu.domain.model.RecordCalendarContent
import com.ilgusu.domain.model.news.ImageInfo
import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.domain.model.stats.WordIdStats
import com.ilgusu.domain.model.stats.WordStats
import com.ilgusu.domain.repository.RecordRepository
import com.ilgusu.domain.repository.StatsRepository
import java.io.File
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val dataSource: StatsRemoteDataSource,
) : StatsRepository {

    override suspend fun searchWords(keyword: String): Result<List<WordIdStats>> {
        return try {
            val response = dataSource.searchWords(keyword)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.map { WordIdStats(it.keywordId, it.noun) })
                } else {
                    throw Exception("Body is null")
                }
            } else {
                throw Exception("Request is failure")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecentRecordWord(): Result<List<WordIdStats>> {
        return try {
            val response = dataSource.getRecentRecordWord()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.map { WordIdStats(it.keywordId, it.noun) })
                } else {
                    throw Exception("Body is null")
                }
            } else {
                throw Exception("Request is failure")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun myWordStats(): Result<WordStats> {
        return try {
            val response = dataSource.myWordStats()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.let {
                        WordStats(
                            count = it.count,
                            noun = it.noun,
                            avgDailyRecord = it.avgDailyRecord,
                            avgWeeklyRecord = it.avgWeeklyRecord,
                            lastRecordedAt = it.lastRecordedAt
                        )
                    })
                } else {
                    throw Exception("Body is null")
                }
            } else {
                throw Exception("Request is failure")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun allWordStats(): Result<WordStats> {
        return try {
            val response = dataSource.allWordStats()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.let {
                        WordStats(
                            count = it.count,
                            noun = it.noun,
                            avgDailyRecord = it.avgDailyRecord,
                            avgWeeklyRecord = it.avgWeeklyRecord,
                            lastRecordedAt = it.lastRecordedAt
                        )
                    })
                } else {
                    throw Exception("Body is null")
                }
            } else {
                throw Exception("Request is failure")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}