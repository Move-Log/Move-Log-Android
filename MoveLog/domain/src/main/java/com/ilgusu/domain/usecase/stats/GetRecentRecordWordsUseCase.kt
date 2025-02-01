package com.ilgusu.domain.usecase.stats

import com.ilgusu.domain.model.stats.WordIdStats
import com.ilgusu.domain.repository.StatsRepository
import javax.inject.Inject

class GetRecentRecordWordsUseCase @Inject constructor(
    private val repository: StatsRepository
) {

    suspend operator fun invoke(): Result<List<WordIdStats>> {
        return repository.getRecentRecordWord()
    }
}