package com.ilgusu.domain.usecase.stats

import com.ilgusu.domain.model.stats.WordIdStats
import com.ilgusu.domain.model.stats.WordStats
import com.ilgusu.domain.repository.StatsRepository
import javax.inject.Inject

class GetMyWordStatsUseCase @Inject constructor(
    private val repository: StatsRepository
) {

    suspend operator fun invoke(): Result<WordStats> {
        return repository.myWordStats()
    }
}