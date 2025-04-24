package com.franksap2.finances.ui.stockdetail.components.grapth.line

import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import com.franksap2.finances.ui.stockdetail.CandleUi
import com.franksap2.finances.ui.stockdetail.components.grapth.GraphState
import com.franksap2.finances.ui.stockdetail.components.grapth.dragIndicatorRadius
import com.franksap2.finances.utils.formatToChartDay

@Composable
internal fun DragIndicator(
    graphState: GraphState,
    currentCandleSelected: CandleUi?,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colors.onSurface,
    indicatorColor: Color = MaterialTheme.colors.primary,
    indicatorBgColor: Color = MaterialTheme.colors.background,
) {

    Canvas(modifier) {
        if (currentCandleSelected != null) {
            val progress = size.width * graphState.remainTime
            val pointX = (progress / graphState.candleData.size) * graphState.resolveSelectedCandle(size.width)


            val yPoint = graphState.calculateYPoint(height = size.height, sessionPrice = currentCandleSelected.closedPrice)

            val dateText = currentCandleSelected.timeStamp.formatToChartDay(graphState.timeSelector)
            val textLayoutResult = graphState.measureText(text = dateText)

            drawLine(
                color = lineColor,
                start = Offset(pointX, textLayoutResult.size.height.toFloat() + 4.dp.toPx()),
                end = Offset(pointX, size.height - 4.dp.toPx()),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )

            val indicatorCenter = Offset(pointX, yPoint)

            drawCircle(
                color = indicatorBgColor,
                radius = dragIndicatorRadius.toPx(),
                center = indicatorCenter
            )
            drawCircle(
                color = indicatorColor,
                radius = dragIndicatorRadius.toPx() * 0.7f,
                center = indicatorCenter
            )


            val textPostX = (pointX - textLayoutResult.size.width / 2)
                .coerceIn(0f, size.width - textLayoutResult.size.width)

            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(textPostX, 0F),
                color = indicatorColor,
            )
        }

    }
}

