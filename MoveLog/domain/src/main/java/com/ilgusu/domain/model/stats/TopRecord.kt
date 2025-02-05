package com.ilgusu.domain.model.stats

data class TopRecord(
    val count: Int,
    val keyword: String,
    val rank: Int,
    val trend: String
)