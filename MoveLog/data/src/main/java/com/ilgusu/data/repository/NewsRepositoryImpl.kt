package com.ilgusu.data.repository

import com.ilgusu.data.datasource.remote.NewsRemoteDataSource
import com.ilgusu.data.model.news.CreateNewsHeadLineRequestDTO
import com.ilgusu.domain.model.Pageable
import com.ilgusu.domain.model.news.NewsContent
import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.domain.repository.NewsRepository
import java.io.File
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val dataSource: NewsRemoteDataSource,
) : NewsRepository {

    override suspend fun postNews(keywordId: Int, file: File, headLine: String): Result<Boolean> {
        return try {
            val response = dataSource.postNews(
                file = file, headLine = headLine, keywordId = keywordId
            )

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

    override suspend fun createNewsHeadlines(keywordId: Int, option: String): Result<List<String>> {
        return try {
            val response =
                dataSource.createNewsHeadlines(keywordId, CreateNewsHeadLineRequestDTO(option))

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.body.information.map { it.headline })
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

    override suspend fun recommendNewsKeywords(): Result<List<RecommendKeyword>> {
        return try {
            val response = dataSource.recommendNewsKeywords()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.body.information.map {
                        RecommendKeyword(
                            it.keywordId,
                            it.verb,
                            it.noun
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

    override suspend fun getTodayNewsRecords(): Result<Int> {
        return try {
            val response = dataSource.getTodayNewsRecords()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.body.information.newsStatus)
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

    override suspend fun getWeekNewsRecords(page: Int): Result<Pageable<NewsContent>> {
        return try {
            val response = dataSource.getWeekNewsRecords(page)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.body.information.let { it ->
                        Pageable(
                            content = it.content.map {
                                NewsContent(
                                    createdAt = it.createdAt,
                                    headLine = it.headLine,
                                    newsId = it.newsId,
                                    newsImageUrl = it.newsImageUrl,
                                    verb = it.verb,
                                    noun = it.noun
                                )
                            }, last = it.last, totalPage = it.totalPages
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

    override suspend fun getDateNewsRecords(
        date: String,
        page: Int
    ): Result<Pageable<NewsContent>> {
        return try {
            val response = dataSource.getDateNewsRecords(date, page)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.body.information.let { it ->
                        Pageable(
                            content = it.content.map {
                                NewsContent(
                                    createdAt = it.createdAt,
                                    headLine = it.headLine,
                                    newsId = it.newsId,
                                    newsImageUrl = it.newsImageUrl,
                                    verb = it.verb,
                                    noun = it.noun
                                )
                            }, last = it.last, totalPage = it.totalPages
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