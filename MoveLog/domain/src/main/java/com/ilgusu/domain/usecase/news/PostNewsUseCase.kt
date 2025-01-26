package com.ilgusu.domain.usecase.news

import com.ilgusu.domain.repository.NewsRepository
import java.io.File
import javax.inject.Inject

class PostNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(keywordId: Int, headLine: String, file: File): Result<Boolean> {
        return repository.postNews(keywordId, file, headLine)
    }
}