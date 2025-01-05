package com.ilgusu.domain.usecase.record

import com.ilgusu.domain.repository.RecordRepository
import java.io.File
import javax.inject.Inject

class RecordUseCase @Inject constructor(
    private val repository: RecordRepository
) {

    suspend operator fun invoke(file: File?, type: Int, word: String): Result<Boolean> {
        return repository.record(file, type, word)
    }
}