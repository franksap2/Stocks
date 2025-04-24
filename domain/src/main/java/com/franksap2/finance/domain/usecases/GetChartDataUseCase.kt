package com.franksap2.finance.domain.usecases

import com.franksap2.finance.domain.model.CharData
import com.franksap2.finance.domain.model.CharSection
import com.franksap2.finance.domain.model.Stock
import com.franksap2.finance.domain.model.StockChartData
import com.franksap2.finance.domain.model.TimeSelector
import com.franksap2.finance.domain.repositories.TickerRepository
import com.franksap2.finance.domain.utils.DateResolver
import java.time.Instant
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class GetChartDataUseCase @Inject constructor(
    private val tickerRepository: TickerRepository,
    private val dateResolver: DateResolver,
) {

    private companion object {
        const val AFTER_MARKET = -2
        const val PRE_MARKET = 1
        const val MARKET = 0
    }

    suspend operator fun invoke(timeSelector: TimeSelector): Result<StockChartData> =
        tickerRepository.getHistoricCandle(timeSelector).map {
            it.mapHistoricDataToTimeSelector(timeSelector = timeSelector)
        }


    private fun Stock.mapHistoricDataToTimeSelector(timeSelector: TimeSelector): StockChartData {

        var previousSection = -1
        val sections = HashMap<Int, CharSection>()
        val sectionList = ArrayList<Int>()
        var highest = 0f
        var lowest = Float.MAX_VALUE

        val chartData = candles.mapIndexed { index, chart ->

            highest = max(highest, chart.closedPrice)
            lowest = min(lowest, chart.closedPrice)

            val type = timeSelector.resolveSectionType(chart.timeStamp)

            if (type == previousSection || previousSection == -1) {
                sectionList.add(index)
                sections[type] = CharSection(
                    from = sectionList.first(),
                    to = sectionList.last()
                )
            } else {
                sectionList.clear()
            }

            previousSection = type

            CharData(
                closedPrice = chart.closedPrice,
                openPrice = chart.openPrice,
                highestPrice = chart.highestPrice,
                lowestPrice = chart.lowestPrice,
                timeStamp = chart.timeStamp,
                graphSection = previousSection
            )
        }


        val reamingTime = if (timeSelector == TimeSelector.DAY && chartData.isNotEmpty()) {
            dateResolver.remainTime(chartData.last().timeStamp)
        } else {
            1f
        }

        return StockChartData(
            highestPrice = highest,
            lowestPrice = lowest,
            candle = chartData,
            timeSelector = timeSelector,
            remainTime = reamingTime,
            graphSections = sections,
            tickerOpenPrice = tickerOpenPrice
        )
    }

    private fun TimeSelector.resolveSectionType(timeStamp: Instant): Int {
        return when (this) {
            TimeSelector.DAY -> when {
                dateResolver.isAfterMarket(timeStamp) -> AFTER_MARKET
                dateResolver.isPreMarket(timeStamp) -> PRE_MARKET
                else -> MARKET
            }

            TimeSelector.WEEK -> dateResolver.getDayFromTimeStamp(timeStamp)
            TimeSelector.MONTH -> dateResolver.getWeekFromTimeStamp(timeStamp)
            TimeSelector.THREE_MONTHS -> dateResolver.getMonthFromTimeStamp(timeStamp)
            TimeSelector.YEAR -> dateResolver.getQuarterFromTimeStamp(timeStamp)
            TimeSelector.FIVE_YEARS -> dateResolver.getYear(timeStamp)
        }
    }

}