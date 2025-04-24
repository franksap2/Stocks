package com.franksap2.finances.ui.stockdetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.franksap2.finances.ui.stockdetail.StockDetailIntent
import com.franksap2.finances.ui.stockdetail.StockDetailUiState
import com.franksap2.finances.ui.stockdetail.components.details.StockInformation
import com.franksap2.finances.ui.stockdetail.components.details.StockRecommendation
import com.franksap2.finances.ui.stockdetail.components.grapth.Chart
import com.franksap2.finances.ui.stockdetail.components.grapth.GraphTimeSelector
import com.franksap2.finances.ui.stockdetail.components.header.ChartHeader
import com.franksap2.finances.ui.stockdetail.components.header.StockDetailHeader
import com.franksap2.finances.ui.stockdetail.components.header.TickerText

@Composable
internal fun StockDetailContent(
    state: StockDetailUiState.Content,
    sendIntent: (StockDetailIntent) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(), state.currentChartSelection == null)
            .padding(vertical = 32.dp)
    ) {

        StockDetailHeader(infoState = state.stockInfo)

        TickerText(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            text = state.stockPriceInfo.tickerAmount,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
        )

        ChartHeader(
            modifier = Modifier.padding(horizontal = 16.dp),
            stockChartData = state.stockPriceInfo,
            sendIntent = sendIntent
        )

        Chart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(top = 8.dp),
            sendIntent = sendIntent,
            state = state,
        )

        GraphTimeSelector(
            modifier = Modifier.padding(horizontal = 8.dp),
            sendIntent = sendIntent,
            state = state,
        )

        StockInformation(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            data = state.stockInfo
        )

        StockRecommendation(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            recommendation = state.stockInfo.averageRecommendation,
            isNegative = state.stockPriceInfo.isNegative,
        )

    }
}