package com.franksap2.finances.ui.stockdetail.components.grapth.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.util.lerp
import kotlin.random.Random


internal fun DrawScope.revealMask(
    progress: () -> Float,
    content: DrawScope.() -> Unit,
) {

    drawIntoCanvas { canvas ->

        canvas.withSaveLayer(Rect(Offset.Zero, size), Paint()) {
            content()

            drawRect(
                color = Color.White,
                topLeft = Offset(size.width * progress(), 0f),
                size = size,
                blendMode = BlendMode.DstOut
            )
        }
    }

}

internal fun DrawScope.infiniteRevealMask(
    progressProvider: () -> Float,
    hideMaskOffset: Float = 0f,
    content: DrawScope.() -> Unit,
) {

    drawIntoCanvas { canvas ->

        canvas.withSaveLayer(Rect(Offset.Zero, size), Paint()) {
            content()

            val progress = progressProvider()
            val maskX = lerp(-size.width, size.width, progress)

            drawRect(
                color = Color.White,
                topLeft = Offset(maskX + size.width, progress),
                size = size,
                blendMode = BlendMode.DstOut
            )

            val secondMaskX = (-size.width - hideMaskOffset) * (1 - progress)

            drawRect(
                color = Color.White,
                topLeft = Offset(secondMaskX, 0f),
                size = size,
                blendMode = BlendMode.DstOut
            )
        }
    }

}

internal fun Random.nextFloat(min: Float, max: Float): Float {
    return (nextFloat() * (max - min)) + min
}