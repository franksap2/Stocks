package com.franksap2.finances.ui.stockdetail.mappers

import com.franksap2.finance.domain.model.AverageStockRecommendation
import com.franksap2.finance.domain.model.CharData
import com.franksap2.finance.domain.model.CharSection
import com.franksap2.finance.domain.model.StockChartData
import com.franksap2.finance.domain.model.StockInfo
import com.franksap2.finance.domain.model.StockProfile
import com.franksap2.finance.domain.model.TimeSelector
import com.franksap2.finances.ui.stockdetail.AverageStockRecommendationUi
import com.franksap2.finances.ui.stockdetail.CandleUi
import com.franksap2.finances.ui.stockdetail.CharSectionUi
import com.franksap2.finances.ui.stockdetail.ChartType
import com.franksap2.finances.ui.stockdetail.StockChartDataUi
import com.franksap2.finances.ui.stockdetail.StockInfoUi
import com.franksap2.finances.ui.stockdetail.StockPriceInfoUi
import com.franksap2.finances.ui.stockdetail.StockProfileUi
import com.franksap2.finances.ui.stockdetail.TimeSelectorUi
import com.franksap2.finances.utils.formatToCurrency
import com.franksap2.finances.utils.formatToPercent
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.max
import kotlin.math.min

internal fun StockInfo.toUi(): StockInfoUi = StockInfoUi(
    ceo = ceo,
    address = address,
    description = description,
    stockProfile = stockProfile.toUi(),
    averageRecommendation = averageStockRecommendation.toUi()
)


private fun AverageStockRecommendation.toUi() = AverageStockRecommendationUi(
    buyRating = buyRating,
    holdRating = holdRating,
    sellRating = sellRating,
    totalRatings = totalRatings,
    buyRatingFormatted = buyRating.formatToPercent(decimals = 1),
    holdRatingFormatted = holdRating.formatToPercent(decimals = 1),
    sellRatingFormatted = sellRating.formatToPercent(decimals = 1),
)

internal fun StockProfile.toUi() = StockProfileUi(
    companyName = companyName,
    ticker = ticker,
    logo = logo,
    web = web
)

internal fun StockChartData.toUi(
    chartType: ChartType,
    currentChartSelection: CandleUi?,
): StockPriceInfoUi {

    val sessionPrice = if (timeSelector == TimeSelector.DAY) {
        tickerOpenPrice.previousClosePrice
    } else {
        candle.firstOrNull()?.closedPrice ?: 0f
    }

    val tickerAmount = currentChartSelection?.closedPrice ?: tickerOpenPrice.currentPrice

    val gains = tickerAmount - sessionPrice
    val percentGains = (gains / sessionPrice).takeIf { !it.isNaN() } ?: 0f

    val candleUi = when (chartType) {
        ChartType.LINE -> candle.map { it.toUi() }
        ChartType.CANDLE -> candle.toCandle(timeSelector)
    }

    return StockPriceInfoUi(
        openPrice = tickerOpenPrice.openPrice,
        currentPrice = tickerOpenPrice.currentPrice,
        isNegative = tickerAmount < sessionPrice,
        graphSections = graphSections.mapValues { it.value.toUi() }.toImmutableMap(),
        gainsFormatted = gains.formatToCurrency(prefix = "+"),
        percentGains = percentGains,
        percentGainsFormatted = percentGains.formatToPercent(),
        tickerAmount = tickerAmount.formatToCurrency(),
        stockChartData = StockChartDataUi(
            highestPrice = highestPrice,
            lowestPrice = lowestPrice,
            timeSelector = timeSelector.toUi(),
            remainTime = remainTime,
            candles = candleUi.toImmutableList(),
            graphSections = graphSections.mapValues { it.value.toUi() }.toImmutableMap(),
            sessionPrice = sessionPrice,
            chartType = chartType,
        )
    )
}


private fun List<CharData>.toCandle(timeSelector: TimeSelector): List<CandleUi> {

    val data = ArrayList<CandleUi>()

    val (time, unit) = when (timeSelector) {
        TimeSelector.DAY -> 20L to ChronoUnit.MINUTES
        TimeSelector.WEEK -> 2L to ChronoUnit.HOURS
        TimeSelector.MONTH -> 1L to ChronoUnit.DAYS
        TimeSelector.THREE_MONTHS -> 3L to ChronoUnit.DAYS
        else -> 2L to ChronoUnit.WEEKS
    }

    var candleCloseDate: LocalDateTime? = null
    var openCandleIndex: Int = -1

    for (i in indices step 1) {

        if (openCandleIndex == -1) {
            openCandleIndex = i
        }

        val candle = this[openCandleIndex]

        if (candleCloseDate == null) {
            candleCloseDate = LocalDateTime.ofInstant(candle.timeStamp, ZoneId.systemDefault()).plus(time, unit)
        }

        val currentCandle = this[i]
        val currentCandleDate = LocalDateTime.ofInstant(currentCandle.timeStamp, ZoneId.systemDefault())

        if (currentCandleDate.isAfter(candleCloseDate)) {

            val openCandle = this[openCandleIndex]
            val closeCandle = getOrNull(i - 1) ?: openCandle

            val openPrice = openCandle.openPrice
            val closedPrice = closeCandle.closedPrice

            data.add(
                CandleUi(
                    openPrice = openPrice,
                    closedPrice = closedPrice,
                    highestPrice = max(openCandle.highestPrice, closeCandle.highestPrice),
                    lowestPrice = min(openCandle.lowestPrice, closeCandle.lowestPrice),
                    graphSection = openCandle.graphSection,
                    isNegative = closeCandle.closedPrice < openCandle.openPrice,
                    timeStamp = ZonedDateTime.ofInstant(openCandle.timeStamp, ZoneId.systemDefault()),
                    toTimeStamp = ZonedDateTime.ofInstant(closeCandle.timeStamp, ZoneId.systemDefault()),
                    gains = (closedPrice - openPrice) / openPrice
                )
            )

            if (i == lastIndex) {
                data.add(currentCandle.toUi())
            } else {
                openCandleIndex = i - 1
                candleCloseDate = null
            }
        }
    }

    return data

}

private fun CharData.toUi() = CandleUi(
    openPrice = openPrice,
    closedPrice = closedPrice,
    highestPrice = highestPrice,
    lowestPrice = lowestPrice,
    timeStamp = ZonedDateTime.ofInstant(timeStamp, ZoneId.systemDefault()),
    graphSection = graphSection,
    isNegative = closedPrice < openPrice,
    gains = (closedPrice - openPrice) / openPrice,
)

private fun CharSection.toUi(): CharSectionUi = CharSectionUi(from = from, to = to)

private fun TimeSelector.toUi() = when (this) {
    TimeSelector.DAY -> TimeSelectorUi.DAY
    TimeSelector.WEEK -> TimeSelectorUi.WEEK
    TimeSelector.MONTH -> TimeSelectorUi.MONTH
    TimeSelector.THREE_MONTHS -> TimeSelectorUi.THREE_MONTHS
    TimeSelector.YEAR -> TimeSelectorUi.YEAR
    TimeSelector.FIVE_YEARS -> TimeSelectorUi.FIVE_YEARS
}

internal fun TimeSelectorUi.toDomain() = when (this) {
    TimeSelectorUi.DAY -> TimeSelector.DAY
    TimeSelectorUi.WEEK -> TimeSelector.WEEK
    TimeSelectorUi.MONTH -> TimeSelector.MONTH
    TimeSelectorUi.THREE_MONTHS -> TimeSelector.THREE_MONTHS
    TimeSelectorUi.YEAR -> TimeSelector.YEAR
    TimeSelectorUi.FIVE_YEARS -> TimeSelector.FIVE_YEARS
}