package com.ilgusu.domain.usecase.record

import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.domain.repository.RecordRepository
import javax.inject.Inject

class SearchRecordUseCase @Inject constructor(
    private val repository: RecordRepository
) {

    suspend operator fun invoke(): Result<List<RecommendKeyword>> {
        return repository.searchRecord()
    }
}