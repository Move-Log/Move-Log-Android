package com.ilgusu.data.repository

import com.ilgusu.data.datasource.remote.StatsRemoteDataSource
import com.ilgusu.domain.model.stats.AllRecordStats
import com.ilgusu.domain.model.stats.TopRecord
import com.ilgusu.domain.model.stats.WordIdStats
import com.ilgusu.domain.model.stats.WordStats
import com.ilgusu.domain.repository.StatsRepository
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

    override suspend fun myWordStats(keywordId: Int): Result<WordStats> {
        return try {
            val response = dataSource.myWordStats(keywordId)

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

    override suspend fun allWordStats(keyword: String): Result<WordStats> {
        return try {
            val response = dataSource.allWordStats(keyword)

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

    override suspend fun getAllRecordStats(
        category: String,
        period: String,
        month: String?,
    ): Result<AllRecordStats> {
        return try {
            val response = dataSource.getAllRecordStats(category, period, month)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(
                        AllRecordStats(
                            category = body.category,
                            totalRecords = body.totalRecords,
                            avgDailyRecord = body.avgDailyRecord,
                            topRecords = body.topRecords.map {
                                TopRecord(
                                    count = it.count,
                                    keyword = it.keyword,
                                    rank = it.rank,
                                    trend = it.trend
                                )
                            },
                            maxDailyRecord = body.maxDailyRecord,
                            maxConsecutiveDays = body.maxConsecutiveDays
                        )
                    )
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