package com.franksap2.finance.domain.model

import java.time.Instant


data class StockChartData(
    val highestPrice: Float,
    val lowestPrice: Float,
    val timeSelector: TimeSelector,
    val candle: List<CharData>,
    val remainTime: Float,
    val graphSections: Map<Int, CharSection>,
    val tickerOpenPrice: TickerOpenPrice,
)

data class CharSection(
    val from: Int,
    val to: Int,
)

data class CharData(
    val openPrice: Float,
    val closedPrice: Float,
    val highestPrice: Float,
    val lowestPrice: Float,
    val timeStamp: Instant,
    val graphSection: Int,
)