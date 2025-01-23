package com.ilgusu.presentation.util

import android.icu.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtil {

    /**
     * 현재 날짜를 지정된 포맷으로 반환합니다.
     * @param format 날짜를 포맷팅할 문자열 (예: "yyyy-MM-dd", "MM/dd/yyyy HH:mm:ss" 등)
     * @param locale 로케일 (기본값: Locale.getDefault())
     * @return 포맷팅된 날짜 문자열
     */
    fun getCurrentDate(format: String, locale: Locale = Locale.getDefault()): String {
        val date = Date() // 현재 날짜 및 시간 가져오기
        val formatter = java.text.SimpleDateFormat(format, locale)
        return formatter.format(date)
    }

    /**
     * "yyyy년 MM월 dd일 E요일" 형식으로 현재 날짜 반환
     * @return 예: "2024년 10월 16일 수요일"
     */
    fun getKoreanDateWithDay(): String {
        val date = Date()
        val formatter = java.text.SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN)
        return formatter.format(date)
    }

    /**
     * 현재 시간을 시, 분, 초로 반환
     * @return Triple<Int, Int, Int> (예: 23, 30, 20)
     */
    fun getTimeAsIntTriple(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY) // 24시간제 시
        val minute = calendar.get(Calendar.MINUTE)   // 분
        val second = calendar.get(Calendar.SECOND)   // 초
        return Triple(hour, minute, second)
    }

    fun getRelativeTime(createdAt: String, dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): String {
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())

        return try {
            val createdDate = sdf.parse(createdAt)
            val now = Date()

            val diff = now.time - (createdDate?.time ?: now.time)

            when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "방금 전"
                diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}분 전"
                diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}시간 전"
                diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)}일 전"
                else -> SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(createdDate!!)
            }
        } catch (e: Exception) {
            "알 수 없음"
        }
    }
}