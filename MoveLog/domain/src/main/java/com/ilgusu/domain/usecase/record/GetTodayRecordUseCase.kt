package com.ilgusu.domain.usecase.record

import com.ilgusu.domain.repository.RecordRepository
import javax.inject.Inject

class GetTodayRecordUseCase @Inject constructor(
    private val repository: RecordRepository
) {

    suspend operator fun invoke(): Result<List<Int>> {
        return repository.getTodayRecord()
    }
}