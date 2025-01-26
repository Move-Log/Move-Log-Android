package com.ilgusu.domain.usecase.record

import com.ilgusu.domain.model.Pageable
import com.ilgusu.domain.model.RecordCalendarContent
import com.ilgusu.domain.repository.RecordRepository
import javax.inject.Inject

class GetTodayRecordListUseCase @Inject constructor(
    private val repository: RecordRepository
) {

    suspend operator fun invoke(date:String, page: Int): Result<Pageable<RecordCalendarContent>> {
        return repository.getCalendarRecords(date, page)
    }
}