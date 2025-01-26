package com.ilgusu.domain.usecase.news

import com.ilgusu.domain.model.Pageable
import com.ilgusu.domain.model.news.NewsContent
import com.ilgusu.domain.repository.NewsRepository
import javax.inject.Inject

class GetDateNewsRecordUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(date: String, page: Int = 0): Result<Pageable<NewsContent>> {
        return repository.getDateNewsRecords(date, page)
    }
}