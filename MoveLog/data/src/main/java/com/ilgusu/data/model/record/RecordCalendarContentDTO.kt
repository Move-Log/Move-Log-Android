package com.ilgusu.data.model.record

import com.google.gson.annotations.SerializedName

data class RecordCalendarContentDTO(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("recordId")
    val recordId: Int,
    @SerializedName("recordImageUrl")
    val recordImageUrl: String,
    @SerializedName("noun")
    val noun: String,
    @SerializedName("verb")
    val verb: String
)
