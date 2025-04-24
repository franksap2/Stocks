package com.franksap2.finance.datasource.model

import com.google.gson.annotations.SerializedName


data class StockResponse(
    @SerializedName("ticker_open_price")
    val tickerOpenPrice: TickerResponse,
    @SerializedName("candles")
    val candles: List<HistoricCandleResponse>,
)

data class HistoricCandleResponse(
    @SerializedName("c")
    val closedPrice: Float,
    @SerializedName("h")
    val highestPrice: Float,
    @SerializedName("l")
    val lowestPrice: Float,
    @SerializedName("o")
    val openPrice: Float,
    @SerializedName("t")
    val timeStamp: Long,
)

data class TickerResponse(
    @SerializedName("c")
    val currentPrice: Float,
    @SerializedName("h")
    val highestPrice: Float,
    @SerializedName("l")
    val lowestPrice: Float,
    @SerializedName("o")
    val openPrice: Float,
    @SerializedName("pc")
    val previousClosePrice: Float,
    @SerializedName("t")
    val timeStamp: Long,
)

