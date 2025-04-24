package com.franksap2.finance.datasource.model

import com.google.gson.annotations.SerializedName

data class StockRecommendationResponse(
    @SerializedName("buy")
    val buyRating: Int,
    @SerializedName("hold")
    val holdRating: Int,
    @SerializedName("sell")
    val sellRating: Int,
    @SerializedName("strongBuy")
    val strongBuy: Int,
    @SerializedName("strongSell")
    val strongSell: Int,
)