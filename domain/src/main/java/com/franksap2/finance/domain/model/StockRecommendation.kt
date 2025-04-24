package com.franksap2.finance.domain.model

data class StockRecommendation(
    val buyRating: Int,
    val holdRating: Int,
    val sellRating: Int,
    val strongBuy: Int,
    val strongSell: Int,
)
