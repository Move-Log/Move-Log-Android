package com.ilgusu.data.repository

import com.ilgusu.data.datasource.remote.RecordRemoteDataSource
import com.ilgusu.domain.model.news.ImageInfo
import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.domain.repository.RecordRepository
import java.io.File
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val dataSource: RecordRemoteDataSource,
) : RecordRepository {

    override suspend fun record(file: File?, type: String, word: String): Result<Boolean> {
        return try {
            val response = dataSource.record(file, type, word)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(true)
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

    override suspend fun getTodayRecord(): Result<List<Int>> {
        return try {
            val response = dataSource.getTodayRecord()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val result = mutableListOf<Int>()
                    body.body.information.let {
                        if (it.optionDo) result.add(0)
                        if (it.optionGo) result.add(1)
                        if (it.optionEat) result.add(2)
                    }
                    Result.success(result)
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

    override suspend fun searchRecord(): Result<List<RecommendKeyword>> {
        return try {
            val response = dataSource.searchRecord()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.body.information.map {
                        RecommendKeyword(
                            it.keywordId, it.verb, it.noun
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

    override suspend fun getRecentRecordImages(keywordId: Int): Result<List<ImageInfo>> {
        return try {
            val response = dataSource.getRecentRecordImages(keywordId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.body.information.map {
                        ImageInfo(
                            it.imageUrl, it.createAt
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

    override suspend fun getRecentCurrentImages(): Result<List<String>> {
        return try {
            val response = dataSource.getRecentCurrentImages()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.body.information.map {
                        it.imageUrl
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