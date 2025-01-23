package com.ilgusu.domain.repository

import com.ilgusu.domain.model.Pageable
import com.ilgusu.domain.model.news.NewsContent
import com.ilgusu.domain.model.news.RecommendKeyword
import java.io.File

interface NewsRepository {

    suspend fun postNews(
        keywordId: Int,
        file: File,
        headLine: String
    ): Result<Boolean>

    suspend fun createNewsHeadlines(
        keywordId: Int,
        option: String
    ): Result<List<String>>

    suspend fun recommendNewsKeywords(): Result<List<RecommendKeyword>>

    suspend fun getTodayNewsRecords(): Result<Int>

    suspend fun getWeekNewsRecords(page: Int = 0): Result<Pageable<NewsContent>>

    suspend fun getDateNewsRecords(
        date: String,
        page: Int = 0
    ): Result<Pageable<NewsContent>>
}