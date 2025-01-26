package com.ilgusu.data.model.news

import com.google.gson.annotations.SerializedName

data class GetDateNewsRecordResponseDTO(
    @SerializedName("content")
    val content: List<NewsContentDTO>,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("totalPages")
    val totalPages: Int
)


