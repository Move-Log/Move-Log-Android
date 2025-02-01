package com.ilgusu.domain.usecase.stats

import com.ilgusu.domain.model.stats.WordIdStats
import com.ilgusu.domain.repository.StatsRepository
import javax.inject.Inject

class SearchWordsUseCase @Inject constructor(
    private val repository: StatsRepository
) {

    suspend operator fun invoke(keyword: String): Result<List<WordIdStats>> {
        return repository.searchWords(keyword)
    }
}