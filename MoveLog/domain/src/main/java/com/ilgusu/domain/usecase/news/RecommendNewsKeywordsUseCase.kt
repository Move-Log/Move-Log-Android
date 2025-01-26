package com.ilgusu.domain.usecase.news

import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.domain.repository.NewsRepository
import javax.inject.Inject

class RecommendNewsKeywordsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(): Result<List<RecommendKeyword>> {
        return repository.recommendNewsKeywords()
    }
}