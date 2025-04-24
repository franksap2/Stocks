package com.franksap2.finances.ui.stockdetail

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.franksap2.finances.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import java.time.ZonedDateTime

sealed interface StockDetailUiState {

    data class Content(
        val stockPriceInfo: StockPriceInfoUi,
        val stockInfo: StockInfoUi,
        val currentChartSelection: CandleUi?,
    ) : StockDetailUiState

    data object Loading : StockDetailUiState

}

sealed interface StockDetailIntent {
    data class CurrentSelection(val charData: CandleUi) : StockDetailIntent
    data object ClearSelection : StockDetailIntent
    data class TimeSelected(val timeSelector: TimeSelectorUi) : StockDetailIntent
    data object ChangeToLineGraph : StockDetailIntent
    data object ChangeToCandleGraph : StockDetailIntent
}

data class StockInfoUi(
    val ceo: String,
    val address: String,
    val description: String,
    val stockProfile: StockProfileUi,
    val averageRecommendation: AverageStockRecommendationUi,
)

data class AverageStockRecommendationUi(
    val buyRating: Float,
    val buyRatingFormatted: String,
    val holdRating: Float,
    val holdRatingFormatted: String,
    val sellRating: Float,
    val sellRatingFormatted: String,
    val totalRatings: Int,
)

data class StockProfileUi(
    val companyName: String,
    val ticker: String,
    val logo: String,
    val web: String,
)


data class StockPriceInfoUi(
    val openPrice: Float,
    val currentPrice: Float,
    val isNegative: Boolean,
    val graphSections: ImmutableMap<Int, CharSectionUi>,
    val gainsFormatted: String,
    val percentGainsFormatted: String,
    val percentGains: Float,
    val tickerAmount: String,
    val stockChartData: StockChartDataUi,
)

data class StockChartDataUi(
    val highestPrice: Float,
    val lowestPrice: Float,
    val timeSelector: TimeSelectorUi,
    val remainTime: Float,
    val candles: ImmutableList<CandleUi>,
    val graphSections: ImmutableMap<Int, CharSectionUi>,
    val sessionPrice: Float,
    val chartType: ChartType,
)

data class CharSectionUi(
    val from: Int,
    val to: Int,
)

@Immutable
data class CandleUi(
    val openPrice: Float,
    val closedPrice: Float,
    val highestPrice: Float,
    val lowestPrice: Float,
    val graphSection: Int,
    val timeStamp: ZonedDateTime,
    val isNegative: Boolean,
    val gains: Float,
    val toTimeStamp: ZonedDateTime = timeStamp,
)


enum class TimeSelectorUi(@StringRes val text: Int) {
    DAY(R.string.one_day),
    WEEK(R.string.week),
    MONTH(R.string.month),
    THREE_MONTHS(R.string.three_months),
    YEAR(R.string.one_year),
    FIVE_YEARS(R.string.five_years)
}

enum class ChartType {
    LINE,
    CANDLE
}


internal fun StockDetailUiState.toContentKey(): String {
    return when (this) {
        is StockDetailUiState.Content -> "Content"
        is StockDetailUiState.Loading -> "Loading"
    }
}
