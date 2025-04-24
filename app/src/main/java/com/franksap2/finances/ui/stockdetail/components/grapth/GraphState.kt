package com.franksap2.finances.ui.stockdetail.components.grapth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.lerp
import com.franksap2.finances.ui.stockdetail.CandleUi
import com.franksap2.finances.ui.stockdetail.CharSectionUi
import com.franksap2.finances.ui.stockdetail.ChartType
import com.franksap2.finances.ui.stockdetail.StockChartDataUi
import com.franksap2.finances.ui.stockdetail.TimeSelectorUi


@Composable
internal fun rememberGraphState(
    textStyle: TextStyle,
    data: StockChartDataUi,
): GraphState {

    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val state = remember {
        GraphState(
            textMeasurer = textMeasurer,
            density = density,
            textStyle = textStyle
        )
    }

    LaunchedEffect(data) { state.animateTo(data) }

    return state
}

@Stable
internal class GraphState(
    val textMeasurer: TextMeasurer,
    private val textStyle: TextStyle,
    private val density: Density,
) {

    private var dragPosition = 0f

    private var _selectedCandleCenterX by mutableFloatStateOf(0f)
    val selectedCandleCenterX get() = _selectedCandleCenterX

    private val revealGraph = Animatable(0f)
    val revealProgress get() = revealGraph.value * remainTime

    private var _chartType by mutableStateOf(ChartType.LINE)
    val chartType get() = _chartType

    private var _candleData by mutableStateOf(emptyList<CandleUi>())
    val candleData get() = _candleData

    private var _graphSections by mutableStateOf<Map<Int, CharSectionUi>>(emptyMap())
    val graphSections get() = _graphSections

    private var _remainTime by mutableFloatStateOf(0f)
    val remainTime get() = _remainTime

    private var _timeSelector by mutableStateOf(TimeSelectorUi.DAY)
    val timeSelector get() = _timeSelector

    private var _sessionPrice by mutableFloatStateOf(0f)
    val sessionPrice get() = _sessionPrice

    private var lowestPrice = 0f
    private var highestPrice = 0f

    val textIndicatorHeight: Float by lazy {
        val dragIndicatorRadiusPx = with(density) { dragIndicatorRadius.toPx() }
        val defaultTextSizeResult = textMeasurer.measure(text = "0", style = textStyle)
        defaultTextSizeResult.size.height.toFloat() + dragIndicatorRadiusPx
    }

    fun measureText(text: String) = textMeasurer.measure(text = text, style = textStyle)

    fun calculateYPoint(height: Float, sessionPrice: Float): Float {

        val percent = (1 - ((sessionPrice - lowestPrice) / (highestPrice - lowestPrice))).coerceIn(0f, 1f)

        return lerp(textIndicatorHeight, height - textIndicatorHeight, percent)
    }

    fun resolveSelectedCandle(width: Float): Int {
        val progress = width * remainTime
        val draggedPercent = (dragPosition.coerceIn(0f, progress)) / progress
        return (candleData.lastIndex * draggedPercent).toInt()
    }

    suspend fun animateTo(data: StockChartDataUi) {
        revealGraph.snapTo(0f)

        highestPrice = data.highestPrice
        lowestPrice = data.lowestPrice

        _timeSelector = data.timeSelector
        _remainTime = data.remainTime
        _candleData = data.candles
        _graphSections = data.graphSections
        _sessionPrice = data.sessionPrice
        _chartType = data.chartType

        revealGraph.animateTo(1f, tween(1500))
    }

    fun setDragPosition(dragPosition: Float) {
        this.dragPosition = dragPosition
    }

    fun translateDragPosition(dragPosition: Float) {
        this.dragPosition += dragPosition
    }

    fun setSelectedCandleCenterX(centerX: Float) {
        _selectedCandleCenterX = centerX
    }

}