package com.ilgusu.domain.usecase.news

import com.ilgusu.domain.repository.NewsRepository
import javax.inject.Inject

class GetTodayNewsRecordUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(): Result<Int> {
        return repository.getTodayNewsRecords()
    }
}