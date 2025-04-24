package com.fransap2.finance.data.info

import com.franksap2.finance.datasource.model.StockInfoResponse
import com.franksap2.finance.datasource.model.StockProfileResponse
import com.franksap2.finance.datasource.model.StockRecommendationResponse
import com.franksap2.finance.domain.model.AverageStockRecommendation
import com.franksap2.finance.domain.model.StockInfo
import com.franksap2.finance.domain.model.StockProfile

internal fun StockInfoResponse.toDomain(): StockInfo = StockInfo(
    ceo = ceo,
    address = address,
    description = description,
    stockProfile = stockProfile.toDomain(),
    averageStockRecommendation = recommendations.toAverageRecommendation()
)

private fun StockProfileResponse.toDomain(): StockProfile = StockProfile(
    companyName = name,
    ticker = ticker,
    logo = logo,
    web = web
)

private fun List<StockRecommendationResponse>.toAverageRecommendation(): AverageStockRecommendation {

    var averageBuyRating = 0f
    var averageHoldRating = 0f
    var averageSellRating = 0f

    forEach {
        averageBuyRating += it.buyRating + it.strongBuy
        averageHoldRating += it.holdRating + it.strongSell
        averageSellRating += it.sellRating
    }

    val totalRatings = (averageBuyRating +
            averageHoldRating +
            averageSellRating).takeIf { it > 0f } ?: 1f

    averageBuyRating /= totalRatings
    averageHoldRating /= totalRatings
    averageSellRating /= totalRatings

    return AverageStockRecommendation(
        buyRating = averageBuyRating,
        holdRating = averageHoldRating,
        sellRating = averageSellRating,
        totalRatings = size
    )

}