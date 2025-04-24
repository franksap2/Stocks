package com.franksap2.finances.ui.stockdetail.components.grapth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onPlaced


@Composable
internal fun OpenMarketPriceIndicator(
    graphState: GraphState,
    modifier: Modifier = Modifier,
    graphColor: Color = MaterialTheme.colors.primary,
) {

    val animator = remember { Animatable(0f) }
    var graphHeight by remember { mutableIntStateOf(0) }

    // The session price could change affecting the graph height,
    // so an animate animateFloatAsState will make the line jump
    LaunchedEffect(graphState.sessionPrice, graphHeight) {
        val yPoint = graphState.calculateYPoint(height = graphHeight.toFloat(), sessionPrice = graphState.sessionPrice)
        animator.animateTo(
            targetValue = yPoint,
            animationSpec = tween(800)
        )
    }

    Canvas(
        modifier = modifier.onPlaced { graphHeight = it.size.height }
    ) {
        drawLine(
            color = graphColor,
            start = Offset(0f, animator.value),
            end = Offset(size.width, animator.value),
            strokeWidth = 4f,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(1f, 14f), 0f)
        )
    }
}
