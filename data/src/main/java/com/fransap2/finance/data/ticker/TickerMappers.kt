package com.fransap2.finance.data.ticker

import com.franksap2.finance.datasource.dtos.TimeSelectorDto
import com.franksap2.finance.datasource.model.StockResponse
import com.franksap2.finance.datasource.model.TickerResponse
import com.franksap2.finance.domain.model.HistoricCandle
import com.franksap2.finance.domain.model.Stock
import com.franksap2.finance.domain.model.TickerOpenPrice
import com.franksap2.finance.domain.model.TimeSelector
import com.fransap2.finance.data.utils.adjustApiTimeStamp
import java.time.Instant

internal fun TimeSelector.toDto(): TimeSelectorDto {
    return when (this) {
        TimeSelector.DAY -> TimeSelectorDto.DAY
        TimeSelector.WEEK -> TimeSelectorDto.WEEK
        TimeSelector.MONTH -> TimeSelectorDto.MONTH
        TimeSelector.THREE_MONTHS -> TimeSelectorDto.THREE_MONTHS
        TimeSelector.YEAR -> TimeSelectorDto.YEAR
        TimeSelector.FIVE_YEARS -> TimeSelectorDto.FIVE_YEARS
    }
}

internal fun StockResponse.toDomain(): Stock = Stock(
    candles = candles.map {
        HistoricCandle(
            closedPrice = it.closedPrice,
            highestPrice = it.highestPrice,
            lowestPrice = it.lowestPrice,
            openPrice = it.openPrice,
            timeStamp = Instant.ofEpochMilli(it.timeStamp).adjustApiTimeStamp()
        )
    },
    tickerOpenPrice = tickerOpenPrice.toDomain()
)


private fun TickerResponse.toDomain(): TickerOpenPrice = TickerOpenPrice(
    currentPrice = currentPrice,
    highestPrice = highestPrice,
    lowestPrice = lowestPrice,
    openPrice = openPrice,
    previousClosePrice = previousClosePrice,
    timeStamp = Instant.ofEpochMilli(timeStamp).adjustApiTimeStamp()
)
