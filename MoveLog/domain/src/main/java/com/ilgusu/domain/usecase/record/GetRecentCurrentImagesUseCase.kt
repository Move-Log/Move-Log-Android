package com.ilgusu.domain.usecase.record

import com.ilgusu.domain.repository.RecordRepository
import javax.inject.Inject

class GetRecentCurrentImagesUseCase @Inject constructor(
    private val repository: RecordRepository
) {

    suspend operator fun invoke(): Result<List<String>> {
        return repository.getRecentCurrentImages()
    }
}