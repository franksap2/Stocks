package com.franksap2.finances.utils


import com.franksap2.finances.ui.stockdetail.TimeSelectorUi
import java.text.DecimalFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Currency

fun Float.formatToCurrency(prefix: String = ""): String {
    val result = DecimalFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance("USD")
    }.format(this)

    return if (this > 0) {
        "$prefix$result"
    } else {
        result
    }
}

fun Float.formatToPercent(
    decimals: Int = 2,
): String {
    return DecimalFormat.getPercentInstance().apply {
        minimumFractionDigits = decimals
        maximumFractionDigits = decimals
    }.format(this)
}

fun ZonedDateTime.formatToChartDay(timeSelector: TimeSelectorUi): String {

    return when (timeSelector) {
        TimeSelectorUi.DAY -> format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        TimeSelectorUi.MONTH,
        TimeSelectorUi.WEEK,
            -> format(DateTimeFormatter.ofPattern("d MMM, h:mm"))

        else -> format(DateTimeFormatter.ofPattern("d MMM, yyyy"))
    }
}