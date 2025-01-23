package com.ilgusu.domain.repository

import java.io.File

interface RecordRepository {

    suspend fun record(file: File?, type: String, word: String): Result<Boolean>

    suspend fun getTodayRecord(): Result<List<Int>>
}