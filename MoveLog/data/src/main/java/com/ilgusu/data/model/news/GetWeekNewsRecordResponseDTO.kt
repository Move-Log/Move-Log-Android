package com.ilgusu.data.model.news

import com.google.gson.annotations.SerializedName

data class GetWeekNewsRecordResponseDTO(
    @SerializedName("content")
    val content: List<NewsContentDTO>,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("totalPages")
    val totalPages: Int
)


