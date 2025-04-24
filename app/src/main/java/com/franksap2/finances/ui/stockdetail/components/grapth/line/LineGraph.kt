package com.franksap2.finances.ui.stockdetail.components.grapth.line

import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.franksap2.finances.ui.stockdetail.CandleUi
import com.franksap2.finances.ui.stockdetail.components.grapth.GraphState
import com.franksap2.finances.ui.stockdetail.components.grapth.dragIndicatorRadius
import com.franksap2.finances.ui.stockdetail.components.grapth.utils.revealMask
import com.franksap2.finances.utils.formatToChartDay

private val graphStrokeWidth = 3.dp

@Composable
internal fun LineGraph(
    currentChartDataSelected: CandleUi?,
    graphState: GraphState,
    modifier: Modifier = Modifier,
    graphColor: Color = MaterialTheme.colors.primary,
    lineColor: Color = MaterialTheme.colors.onSurface,
    indicatorBgColor: Color = MaterialTheme.colors.background,
) {

    var lineGraphSize by remember { mutableStateOf(IntSize.Zero) }
    val path = remember { Path() }
    val gradientPath = remember { Path() }

    LaunchedEffect(graphState.timeSelector, lineGraphSize) {
        buildLineGraph(
            mainGraph = path,
            gradientPath = gradientPath,
            graphState = graphState,
            size = lineGraphSize
        )
    }

    Canvas(modifier = modifier.onPlaced { lineGraphSize = it.size }) {
        revealMask(progress = { graphState.revealProgress }) {

            drawPath(
                path = path,
                color = graphColor,
                style = Stroke(
                    width = graphStrokeWidth.toPx(),
                    join = StrokeJoin.Round,
                    cap = StrokeCap.Round,
                )
            )

            drawPath(
                path = gradientPath,
                brush = Brush.verticalGradient(
                    listOf(graphColor, Color.Transparent)
                )
            )

            currentChartDataSelected?.let {
                drawHighLightMarketSection(graphSection = it.graphSection, state = graphState)
                drawIndicator(
                    currentCandleSelected = it,
                    graphState = graphState,
                    indicatorColor = graphColor,
                    indicatorBgColor = indicatorBgColor,
                    lineColor = lineColor
                )
            }
        }
    }
}

private fun buildLineGraph(
    mainGraph: Path,
    gradientPath: Path,
    size: IntSize,
    graphState: GraphState,
) {
    mainGraph.reset()
    gradientPath.reset()

    val with = size.width * graphState.remainTime
    val height = size.height.toFloat()
    val count = graphState.candleData.size
    val stepCandleSize = with / count

    var prevX = 0f
    var firstPointY = 0f

    graphState.candleData.fastForEachIndexed { index, value ->

        val yPoint = graphState.calculateYPoint(height = height, sessionPrice = value.closedPrice)

        if (index == 0) {
            firstPointY = yPoint
            mainGraph.moveTo(prevX, yPoint)
            gradientPath.moveTo(prevX, yPoint)
        }

        mainGraph.lineTo(prevX, yPoint)

        gradientPath.lineTo(prevX, yPoint)

        prevX += stepCandleSize
    }

    gradientPath.apply {
        lineTo(prevX, height)

        lineTo(0f, height)

        lineTo(0f, firstPointY)
        close()
    }
}


private fun DrawScope.drawHighLightMarketSection(
    graphSection: Int,
    state: GraphState,
) {

    state.graphSections[graphSection]?.let {
        val width = size.width * state.remainTime
        val chartDataSize = state.candleData.lastIndex.toFloat()

        val from = width * (it.from / chartDataSize)
        val to = width * (it.to / chartDataSize)
        val color = Color(1f, 1f, 1f, 0.5f)

        drawRect(
            color = color,
            topLeft = Offset(0f, 0f),
            size = Size(from, size.height),
            blendMode = BlendMode.DstOut
        )

        drawRect(
            color = color,
            topLeft = Offset(to, 0f),
            size = Size(width - to, size.height),
            blendMode = BlendMode.DstOut
        )
    }

}

private fun DrawScope.drawIndicator(
    currentCandleSelected: CandleUi,
    graphState: GraphState,
    indicatorColor: Color,
    indicatorBgColor: Color,
    lineColor: Color,
) {

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