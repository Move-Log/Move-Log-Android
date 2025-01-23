package com.ilgusu.data.model.news

import com.google.gson.annotations.SerializedName

data class GetTodayNewsRecordResponseDTO(
    @SerializedName("newsStatus")
    val newsStatus: Int
)
