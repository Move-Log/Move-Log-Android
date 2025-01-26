package com.ilgusu.data.model.record

import com.google.gson.annotations.SerializedName

data class GetDateRecordCalendarResponseDTO(
    @SerializedName("content")
    val content: List<RecordCalendarContentDTO>,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("totalPages")
    val totalPages: Int
)
