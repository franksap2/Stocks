package com.franksap2.finance.domain.model

data class StockInfo(
    val ceo: String,
    val address: String,
    val description: String,
    val stockProfile: StockProfile,
    val averageStockRecommendation: AverageStockRecommendation,
)

data class StockProfile(
    val companyName: String,
    val ticker: String,
    val logo: String,
    val web: String,
)

data class AverageStockRecommendation(
    val buyRating: Float,
    val holdRating: Float,
    val sellRating: Float,
    val totalRatings: Int,
)
