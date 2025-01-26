package com.ilgusu.domain.usecase.record

import com.ilgusu.domain.model.news.ImageInfo
import com.ilgusu.domain.repository.RecordRepository
import javax.inject.Inject

class GetRecentRecordImagesUseCase @Inject constructor(
    private val repository: RecordRepository
) {

    suspend operator fun invoke(keywordId: Int): Result<List<ImageInfo>> {
        return repository.getRecentRecordImages(keywordId)
    }
}