package com.franksap2.finances.ui.stockdetail.components.grapth

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.franksap2.finances.ui.stockdetail.ChartType
import com.franksap2.finances.ui.stockdetail.StockDetailIntent
import com.franksap2.finances.ui.stockdetail.StockDetailUiState
import com.franksap2.finances.ui.stockdetail.components.grapth.candle.CandleGraph
import com.franksap2.finances.ui.stockdetail.components.grapth.candle.candlePadding
import com.franksap2.finances.ui.stockdetail.components.grapth.line.LineGraph
import com.franksap2.finances.ui.stockdetail.components.grapth.utils.graphDrag
import com.franksap2.finances.ui.theme.negative
import com.franksap2.finances.ui.theme.positive

internal val dragIndicatorRadius = 6.dp

@Composable
internal fun Chart(
    state: StockDetailUiState.Content,
    sendIntent: (StockDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
    indicatorStyle: TextStyle = MaterialTheme.typography.body1,
) {

    val color by animateColorAsState(
        if (state.stockPriceInfo.isNegative) MaterialTheme.colors.negative else MaterialTheme.colors.positive
    )

    val graphState = rememberGraphState(textStyle = indicatorStyle, data = state.stockPriceInfo.stockChartData)

    Box(
        modifier = modifier
            .padding(horizontal = if (graphState.chartType == ChartType.CANDLE) candlePadding else 0.dp)
            .graphDrag(
                graphState = graphState,
                currentChartDataSelected = state.currentChartSelection,
                onSelectSection = { sendIntent(StockDetailIntent.CurrentSelection(it)) },
                onDragEnd = { sendIntent(StockDetailIntent.ClearSelection) }
            )
    ) {

        OpenMarketPriceIndicator(
            modifier = Modifier.fillMaxSize(),
            graphState = graphState,
            graphColor = color
        )

        when (graphState.chartType) {

            ChartType.LINE -> LineGraph(
                modifier = Modifier.fillMaxSize(),
                currentChartDataSelected = state.currentChartSelection,
                graphState = graphState,
                graphColor = color,
            )

            ChartType.CANDLE -> CandleGraph(
                modifier = Modifier.fillMaxSize(),
                graphState = graphState,
                currentChartDataSelected = state.currentChartSelection,
                indicatorTextColor = color,
            )
        }
    }
}
