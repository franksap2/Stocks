package com.franksap2.finance.domain.model

import java.time.Instant

data class Stock(
    val candles: List<HistoricCandle>,
    val tickerOpenPrice: TickerOpenPrice,
)

data class HistoricCandle(
    val closedPrice: Float,
    val highestPrice: Float,
    val lowestPrice: Float,
    val openPrice: Float,
    val timeStamp: Instant,
)

data class TickerOpenPrice(
    val currentPrice: Float,
    val highestPrice: Float,
    val lowestPrice: Float,
    val openPrice: Float,
    val previousClosePrice: Float,
    val timeStamp: Instant,
)