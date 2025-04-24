package com.franksap2.finance.datasource.model

import com.google.gson.annotations.SerializedName

data class StockInfoResponse(
    @SerializedName("ceo")
    val ceo: String,
    @SerializedName("hq_address")
    val address: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("profile")
    val stockProfile: StockProfileResponse,
    @SerializedName("recommendations")
    val recommendations: List<StockRecommendationResponse>,
)

data class StockProfileResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("weburl")
    val web: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("ticker")
    val ticker: String,
)
