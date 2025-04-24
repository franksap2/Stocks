package com.franksap2.finances.ui.stockdetail.components.grapth.candle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.franksap2.finances.ui.stockdetail.CandleUi
import com.franksap2.finances.ui.stockdetail.components.grapth.GraphState
import com.franksap2.finances.ui.stockdetail.components.grapth.utils.revealMask
import com.franksap2.finances.ui.theme.negativeVariant
import com.franksap2.finances.ui.theme.positiveVariant
import com.franksap2.finances.utils.formatToChartDay

private val maxCandleWidth = 6.dp
internal val candlePadding = 16.dp

@Composable
internal fun CandleGraph(
    graphState: GraphState,
    currentChartDataSelected: CandleUi?,
    indicatorTextColor: Color,
    modifier: Modifier = Modifier,
) {

    val positiveColor = MaterialTheme.colors.positiveVariant
    val negativeColor = MaterialTheme.colors.negativeVariant
    val candleLineColor = MaterialTheme.colors.onBackground

    Box(modifier = modifier) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            revealMask(progress = { graphState.revealProgress }) {
                drawCandleGraph(
                    graphState = graphState,
                    positiveColor = positiveColor,
                    negativeColor = negativeColor,
                    lineColor = candleLineColor,
                    currentChartDataSelected = currentChartDataSelected,
                    indicatorTextColor = indicatorTextColor
                )
            }
        }

        CandleDetailBox(
            modifier = Modifier.fillMaxSize(),
            currentChartDataSelected = currentChartDataSelected,
            graphState = graphState
        )

    }

}

private fun DrawScope.drawCandleGraph(
    graphState: GraphState,
    positiveColor: Color,
    negativeColor: Color,
    lineColor: Color,
    currentChartDataSelected: CandleUi?,
    indicatorTextColor: Color,
) {

    val progress = size.width * graphState.remainTime
    val height = size.height
    val count = graphState.candleData.size

    val stepCandleSize = minOf(maxCandleWidth.toPx(), progress / count)
    val gap = (size.width - stepCandleSize * count) / (count - 1)

    graphState.candleData.fastForEachIndexed { index, value ->

        val yClosePoint = graphState.calculateYPoint(height = height, sessionPrice = value.closedPrice)
        val yOpenPoint = graphState.calculateYPoint(height = height, sessionPrice = value.openPrice)
        val yHighPoint = graphState.calculateYPoint(height = height, sessionPrice = value.highestPrice)
        val yLowPoint = graphState.calculateYPoint(height = height, sessionPrice = value.lowestPrice)

        val left = (stepCandleSize + gap) * index
        val centerX = left + stepCandleSize / 2

        val candleSize = Size(stepCandleSize, yClosePoint - yOpenPoint)

        drawLine(
            color = lineColor,
            start = Offset(centerX, yHighPoint),
            end = Offset(centerX, yLowPoint),
            strokeWidth = 1.dp.toPx()
        )

        drawRect(
            color = if (value.isNegative) negativeColor else positiveColor,
            topLeft = Offset(left, yOpenPoint),
            size = candleSize,
        )

        val candleIndex = graphState.resolveSelectedCandle(width = size.width)
        if (candleIndex == index && currentChartDataSelected != null) {
            drawCandleIndicator(
                lineColor = lineColor,
                startX = centerX,
                graphState = graphState,
                currentChartDataSelected = currentChartDataSelected,
                textColor = indicatorTextColor
            )

            graphState.setSelectedCandleCenterX(centerX)
        }

    }
}

private fun DrawScope.drawCandleIndicator(
    textColor: Color,
    lineColor: Color,
    startX: Float,
    graphState: GraphState,
    currentChartDataSelected: CandleUi,
) {
    val dateText = currentChartDataSelected.timeStamp.formatToChartDay(graphState.timeSelector)
    val toText = currentChartDataSelected.toTimeStamp.formatToChartDay(graphState.timeSelector)

    val textLayoutResult = graphState.measureText(text = "$dateText - $toText")

    drawLine(
        color = lineColor,
        start = Offset(startX, textLayoutResult.size.height.toFloat() + 4.dp.toPx()),
        end = Offset(startX, size.height),
        strokeWidth = 1.dp.toPx()
    )

    val textPostX = (startX - textLayoutResult.size.width / 2)
        .coerceIn(0f, size.width - textLayoutResult.size.width)

    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(textPostX, 0F),
        color = textColor,
    )
}
