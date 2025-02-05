package com.ilgusu.domain.usecase.stats

import com.ilgusu.domain.model.stats.AllRecordStats
import com.ilgusu.domain.repository.StatsRepository
import javax.inject.Inject

class GetAllRecordStatsUseCase @Inject constructor(
    private val repository: StatsRepository
) {

    suspend operator fun invoke(
        category: String,
        period: String,
        month: String? = null
    ): Result<AllRecordStats> {
        return repository.getAllRecordStats(category, period, month)
    }
}