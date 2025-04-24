package com.franksap2.finance.domain.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.IsoFields
import javax.inject.Inject

class DateResolver @Inject constructor() {

    private val mockedMarketDay = LocalDate.of(2025, 4, 16)

    private val zoneOffset = ZoneId.systemDefault().rules.getOffset(Instant.now())

    private val openMarkerTime = LocalTime.of(13, 30, 0)
        .plusSeconds(zoneOffset.totalSeconds.toLong())

    private val closedMarkerTime = LocalTime.of(20, 0, 0)
        .plusSeconds(zoneOffset.totalSeconds.toLong())

    private val closedAfterHour = LocalTime.MAX
        .plusSeconds(zoneOffset.totalSeconds.toLong())

    fun remainTime(lastCandleTime: Instant): Float {
        val candleTime = lastCandleTime.toLocalTime().toSecondOfDay()
        return candleTime / closedAfterHour.toSecondOfDay().toFloat()
    }

    fun isAfterMarket(timeStamp: Instant): Boolean =
        timeStamp.toLocalDateTime().isAfter(mockedMarketDay.atTime(closedMarkerTime))

    fun isPreMarket(timeStamp: Instant): Boolean =
        timeStamp.toLocalDateTime().isBefore(mockedMarketDay.atTime(openMarkerTime))

    fun getDayFromTimeStamp(time: Instant) = LocalDateTime.ofInstant(time, zoneOffset).dayOfYear

    fun getWeekFromTimeStamp(timeStamp: Instant): Int {
        val localDate = timeStamp.toLocalDateTime()
        return localDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
    }

    fun getMonthFromTimeStamp(timeStamp: Instant): Int = timeStamp.toLocalDateTime().month.value

    fun getQuarterFromTimeStamp(timeStamp: Instant): Int {
        val date = timeStamp.toLocalDateTime()
        return ((date.month.value - 1) / 3 + 1) + (date.year % mockedMarketDay.year)
    }

    fun getYear(timeStamp: Instant): Int = timeStamp.toLocalDateTime().year

    private fun Instant.toLocalTime(): LocalTime = LocalTime.ofInstant(this, zoneOffset)

    private fun Instant.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, zoneOffset)

}