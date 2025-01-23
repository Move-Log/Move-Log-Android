package com.ilgusu.data.model.record

import com.google.gson.annotations.SerializedName

data class TodayRecordResponseDTO(
    @SerializedName("do")
    val optionDo: Boolean,
    @SerializedName("eat")
    val optionEat: Boolean,
    @SerializedName("go")
    val optionGo: Boolean
)
