package com.ilgusu.data.repository

import com.ilgusu.data.datasource.remote.RecordRemoteDataSource
import com.ilgusu.domain.repository.RecordRepository
import com.ilgusu.util.LoggerUtil
import java.io.File
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val dataSource: RecordRemoteDataSource,
) : RecordRepository {

    override suspend fun record(file: File?, type: Int, word: String): Result<Boolean> {
        return try {
            val response = dataSource.record(file, type, word)
            LoggerUtil.i(response.toString())
            if(response.isSuccessful) {
                val body = response.body()
                if(body != null) {
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
            if(response.isSuccessful) {
                val body = response.body()
                if(body != null) {
                    Result.success(body.information)
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