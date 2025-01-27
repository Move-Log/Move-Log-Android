package com.ilgusu.presentation.util

import android.icu.text.SimpleDateFormat
import com.ilgusu.util.LoggerUtil
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtil {

    private val DEFAULT_LOCALE = Locale.KOREAN
    private const val KOREA_TIME_ZONE = "Asia/Seoul"
    private val koreaTimeZone: TimeZone = TimeZone.getTimeZone(KOREA_TIME_ZONE)
    private val koreaTimeZoneAndroid: android.icu.util.TimeZone = android.icu.util.TimeZone.getTimeZone(KOREA_TIME_ZONE)

    // 한국 시간대로 포맷된 SimpleDateFormat 반환
    private fun createSimpleDateFormat(pattern: String): SimpleDateFormat =
        SimpleDateFormat(pattern, DEFAULT_LOCALE).apply {
            timeZone = koreaTimeZoneAndroid
        }

    // 공통 포맷 리스트 (extractTimeFlexible, getRelativeTime에서 모두 사용)
    private val possibleFormats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy/MM/dd HH:mm:ss",
        "yyyy.MM.dd HH:mm:ss",
        "yyyy-MM-dd",
        "MM/dd/yyyy",
        "yyyy-MM-dd HH:mm",
        "MM-dd-yyyy HH:mm:ss",
        "yyyy.MM.dd"
    )

    /**
     * 현재 날짜를 지정된 포맷으로 반환합니다.
     * @param format 날짜를 포맷팅할 문자열
     */
    fun getCurrentDate(format: String): String =
        createSimpleDateFormat(format).format(Date())

    /**
     * "yyyy년 MM월 dd일 E요일" 형식으로 현재 날짜 반환
     * @return 예: "2024년 10월 16일 수요일"
     */
    fun getKoreanDateWithDay(): String =
        getCurrentDate("yyyy년 MM월 dd일 E요일")

    /**
     * 현재 시간을 시, 분, 초로 반환 (한국 시간 기준)
     * @return Triple<Int, Int, Int> (예: 23, 30, 20)
     */
    fun getTimeAsIntTriple(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance().apply { timeZone = koreaTimeZone }
        return Triple(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )
    }

    /**
     * 다양한 포맷에서 시간 추출 (한국 시간 기준)
     * @param dateTime 날짜 문자열
     * @return "HH:mm" 형식으로 추출된 시간
     */
    fun extractTimeFlexible(dateTime: String): String {
        val outputFormat = DateTimeFormatter.ofPattern("HH:mm", DEFAULT_LOCALE)

        return possibleFormats.firstNotNullOfOrNull { format ->
            try {
                val formatter = DateTimeFormatter.ofPattern(format, DEFAULT_LOCALE)
                if (format.contains("'Z'")) {
                    val zonedDateTime = ZonedDateTime.parse(dateTime, formatter).withZoneSameInstant(java.time.ZoneId.of(KOREA_TIME_ZONE))
                    zonedDateTime.toLocalTime().format(outputFormat)
                } else {
                    val localDateTime = java.time.LocalDateTime.parse(dateTime, formatter)
                    localDateTime.atZone(java.time.ZoneId.of(KOREA_TIME_ZONE)).toLocalTime().format(outputFormat)
                }
            } catch (_: Exception) {
                null
            }
        } ?: "유효하지 않은 형식"
    }

    /**
     * 다양한 포맷에서 상대 시간 계산 (한국 시간 기준)
     * @param createdAt 생성된 날짜 문자열
     * @return 상대 시간 (예: "5분 전", "2일 전")
     */
    fun getRelativeTime(createdAt: String): String {
        val sdfList = possibleFormats.map { createSimpleDateFormat(it) }

        val kstFormat = createSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        return try {
            // 다양한 포맷 시도
            val createdDate = sdfList.firstNotNullOfOrNull { format ->
                runCatching { format.parse(createdAt) }.getOrNull()
            } ?: throw IllegalArgumentException("지원되지 않는 날짜 형식입니다.")

            // KST(한국 표준시)로 변환
            val kstDate = kstFormat.format(createdDate)
            val parsedKstDate = kstFormat.parse(kstDate)

            val now = Date()
            val diff = now.time - (parsedKstDate?.time ?: now.time)

            // 상대 시간 계산
            when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "방금 전"
                diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}분 전"
                diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}시간 전"
                diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)}일 전"
                else -> createSimpleDateFormat("yyyy년 MM월 dd일").format(parsedKstDate!!)
            }
        } catch (e: Exception) {
            "알 수 없음"
        }
    }
}