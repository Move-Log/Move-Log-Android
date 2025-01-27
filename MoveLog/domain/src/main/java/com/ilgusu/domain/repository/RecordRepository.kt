package com.ilgusu.domain.repository

import com.ilgusu.domain.model.Pageable
import com.ilgusu.domain.model.RecordCalendarContent
import com.ilgusu.domain.model.news.ImageInfo
import com.ilgusu.domain.model.news.RecommendKeyword
import java.io.File

interface RecordRepository {

    suspend fun record(file: File?, type: String, word: String): Result<Boolean>

    suspend fun getTodayRecord(): Result<List<Int>>

    suspend fun searchRecord(): Result<List<RecommendKeyword>>

    suspend fun getRecentRecordImages(keywordId: Int): Result<List<ImageInfo>>

    suspend fun getRecentCurrentImages(): Result<List<String>>
    
    suspend fun getCalendarRecords(
        date: String,
        page: Int = 0
    ): Result<Pageable<RecordCalendarContent>>
}