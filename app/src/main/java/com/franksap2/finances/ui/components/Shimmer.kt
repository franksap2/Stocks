package com.franksap2.finances.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import com.franksap2.finances.ui.theme.surfaceVariant

@Composable
fun Shimmer(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surfaceVariant,
    shape: Shape = MaterialTheme.shapes.small,
) {

    Box(
        modifier = modifier
            .background(color, shape)
    )
}

@Composable
fun Modifier.shimmer(): Modifier {

    val alpha by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        )
    )

    return this then Modifier.graphicsLayer { this.alpha = alpha }
}