package com.ilgusu.domain.usecase.news

import com.ilgusu.domain.repository.NewsRepository
import javax.inject.Inject

class CreateNewsHeadLinesUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(keywordId: Int, option: String): Result<List<String>> {
        return repository.createNewsHeadlines(keywordId, option)
    }
}