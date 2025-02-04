package com.ilgusu.presentation.util

import android.icu.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateUtil {

    /**
     * 현재 날짜를 지정된 포맷으로 반환합니다.
     * @param format 날짜를 포맷팅할 문자열
     */
    fun getCurrentDate(format: String): String =
        SimpleDateFormat(format, Locale.KOREAN).format(Date())

    /**
     * 다양한 포맷에서 시간 추출 (한국 시간 기준)
     * @param dateTime 날짜 문자열
     * @return "HH:mm" 형식으로 추출된 시간
     */
    fun extractTimeFlexible(dateTime: String): String {
        val index = dateTime.indexOf("T") + 1
        return dateTime.substring(index, index + 5)
    }

    /**
     * 다양한 포맷에서 상대 시간 계산 (한국 시간 기준)
     * @param createdAt 생성된 날짜 문자열
     * @return 상대 시간 (예: "5분 전", "2일 전")
     */
    fun getRelativeTime(createdAt: String): String {
        val createdDateTime = LocalDateTime.parse(createdAt)
        val now = LocalDateTime.now()
        val duration = Duration.between(createdDateTime, now)

        return when {
            duration.toMinutes() < 1 -> "방금 전"
            duration.toHours() < 1 -> "${duration.toMinutes()}분 전"
            duration.toDays() < 1 -> "${duration.toHours()}시간 전"
            duration.toDays() < 7 -> "${duration.toDays()}일 전"
            else -> createdDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
        }
    }
}