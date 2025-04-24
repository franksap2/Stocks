package com.franksap2.finances.ui.stockdetail.components.grapth.utils

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import com.franksap2.finances.ui.stockdetail.CandleUi
import com.franksap2.finances.ui.stockdetail.components.grapth.GraphState

@Composable
internal fun Modifier.graphDrag(
    graphState: GraphState,
    currentChartDataSelected: CandleUi?,
    onSelectSection: (CandleUi) -> Unit,
    onDragEnd: () -> Unit,
): Modifier {

    val haptic = LocalHapticFeedback.current

    return this then Modifier.pointerInput(graphState.timeSelector) {

        detectDragGesturesAfterLongPress(
            onDragStart = {
                graphState.setDragPosition(it.x)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            onDragEnd = onDragEnd,
            onDrag = { _, offset ->

                graphState.translateDragPosition(dragPosition = offset.x)

                val distance = graphState.resolveSelectedCandle(width = size.width.toFloat())

                graphState.candleData.getOrNull(distance)?.let {
                    if (it != currentChartDataSelected) {
                        onSelectSection(it)
                    }
                }
            }
        )
    }
}