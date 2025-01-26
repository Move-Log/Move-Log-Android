package com.ilgusu.domain.model.news

data class NewsContent(
    val createdAt: String,
    val headLine: String,
    val newsId: Int,
    val newsImageUrl: String,
    val noun: String,
    val verb: String
)
