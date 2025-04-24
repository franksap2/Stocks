package com.franksap2.finances.ui.stockdetail.components.details

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp


@Composable
internal fun PercentProgressBar(
    percent: Float,
    text: String,
    color: Color,
    legendText: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.caption,
) {

    val animatedValue = remember { Animatable(0f) }
    LaunchedEffect(percent) {
        animatedValue.animateTo(percent, animationSpec = tween(2000))
    }

    val textMeasurer = rememberTextMeasurer()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = legendText,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onBackground,
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .align(Alignment.CenterVertically)
        ) {

            val targetSize = size.width * animatedValue.value

            drawIntoCanvas {

                it.withSaveLayer(Rect(Offset.Zero, size), Paint()) {

                    val barHeight = 4.dp.toPx()
                    val barOffset = Offset(0f, size.height / 2f - barHeight / 2f)
                    val cornerRadius = CornerRadius(12f, 12f)

                    drawRoundRect(
                        color = color.copy(0.2f),
                        size = Size(size.width, barHeight),
                        topLeft = barOffset,
                        cornerRadius = cornerRadius
                    )
                    drawRoundRect(
                        color,
                        size = Size(targetSize, barHeight),
                        topLeft = barOffset,
                        cornerRadius = cornerRadius
                    )

                    val textPadding = 4.dp.toPx()

                    val textLayoutResult = textMeasurer.measure(text = text, style = style)
                    val textBounds = textLayoutResult.size

                    val startTextPosition = (targetSize + textPadding).coerceAtMost(size.width - textBounds.width)

                    val boxWidth = textBounds.width.toFloat() + textPadding
                    val maskOffsetX = targetSize.coerceAtMost(size.width - textBounds.width)

                    drawRect(
                        brush = Brush.horizontalGradient(
                            startX = maskOffsetX - textPadding,
                            endX = maskOffsetX + boxWidth + textPadding,
                            colorStops = arrayOf(0.4f to Color.White, 0.9f to Color.Transparent)
                        ),
                        blendMode = BlendMode.DstOut,
                        size = Size(boxWidth, size.height),
                        topLeft = Offset(maskOffsetX, 0f)
                    )

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(startTextPosition, (size.height - textBounds.height) / 2f),
                        color = color
                    )
                }
            }
        }
    }


}