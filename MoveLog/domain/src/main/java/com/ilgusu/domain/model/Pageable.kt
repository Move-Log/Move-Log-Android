package com.ilgusu.domain.model

data class Pageable<T>(
    val content: List<T>,
    val last: Boolean,
    val totalPage: Int
)
