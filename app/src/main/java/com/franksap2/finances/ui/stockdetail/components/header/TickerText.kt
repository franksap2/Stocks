package com.franksap2.finances.ui.stockdetail.components.header

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.WeakHashMap
import kotlin.math.absoluteValue

private const val UP = 1
private const val DOWN = -1

private val defaultAnimationSpec = tween<Float>(250)

@Composable
fun TickerText(
    text: String,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier,
    animationSpec: AnimationSpec<Float> = defaultAnimationSpec,
) {

    val textCarouselState = rememberTextCarouselState()
    LaunchedEffect(text) {
        textCarouselState.animateTo(text, animationSpec)
    }

    val textMeasureResult = remember(textCarouselState.targetText, style) {
        textCarouselState.measureComponent(textCarouselState.targetText, style)
    }


    Canvas(
        modifier = modifier
            .clipToBounds()
            .layout { measurable, _ ->
                val constraint = Constraints.fixed(
                    textMeasureResult.size.width,
                    textMeasureResult.size.height,
                )
                val placeable = measurable.measure(constraint)

                layout(constraint.maxWidth, constraint.maxHeight) { placeable.placeRelative(0, 0) }
            },
        onDraw = {
            if (textCarouselState.isRunning) {
                drawCarousel(
                    text = textCarouselState.targetText,
                    animValue = textCarouselState.animationProgress,
                    style = style,
                    visibleText = textCarouselState.visibleText,
                    tickerTextState = textCarouselState,
                    color = color
                )
            } else {
                drawText(
                    textLayoutResult = textCarouselState.measure(textCarouselState.visibleText, style),
                    color = color
                )
            }
        },
    )
}

private fun DrawScope.drawCarousel(
    text: String,
    style: TextStyle,
    visibleText: String,
    animValue: Float,
    tickerTextState: TickerTextState,
    color: Color,
) {
    var isNegative = false

    for (i in text.length.dec() downTo 0) {
        var charTop = 0f
        val char = text[i]
        val charLeft = tickerTextState.getComponentBoundingBox(i).left

        val currentPosition = visibleText.getOrNull(i)?.digitToIntOrNull() ?: 0
        val newPosition = char.digitToIntOrNull() ?: 0

        if (i == 0 && char == '-') {
            isNegative = true
        }

        if (char.isDigit() && currentPosition != newPosition) {
            val direction = if ((i == 1 && isNegative) || newPosition > currentPosition) UP else DOWN

            val carouselSize = (newPosition - currentPosition).absoluteValue

            val translation = size.height * carouselSize * direction * animValue

            repeat(carouselSize + 1) { carouselDigit ->

                val carouselText = if (direction == UP) {
                    currentPosition + carouselDigit
                } else {
                    currentPosition + (carouselSize.dec() - carouselDigit) - carouselSize.dec()
                }

                translate(left = charLeft, top = charTop + translation) {
                    drawText(
                        textLayoutResult = tickerTextState.measure(carouselText.toString(), style),
                        color = color
                    )
                }

                charTop -= size.height * direction
            }
        } else {
            drawText(
                textLayoutResult = tickerTextState.measure(char.toString(), style),
                topLeft = Offset(charLeft, 0f),
                color = color
            )
        }
    }
}

@Composable
private fun rememberTextCarouselState(): TickerTextState {
    val textMeasurer = rememberTextMeasurer()
    val scope = rememberCoroutineScope()
    return remember { TickerTextState(textMeasurer, scope) }
}

@Stable
private class TickerTextState(
    private val textMeasurer: TextMeasurer,
    private val coroutineScope: CoroutineScope,
) {

    private val animatable = Animatable(0f)
    private var isAnimating: Boolean by mutableStateOf(false)

    val isRunning: Boolean get() = isAnimating

    val animationProgress get() = animatable.value

    private var _visibleText by mutableStateOf("")
    val visibleText: String get() = _visibleText

    private var _targetText by mutableStateOf("")
    val targetText get() = _targetText

    private var enqueuedNextVisibleText: String? = null

    private val digitsTextLayoutResultCache = WeakHashMap<String, WeakReference<TextLayoutResult>>()

    private var componentTextLayoutResult: TextLayoutResult? = null

    fun getComponentBoundingBox(index: Int): Rect = componentTextLayoutResult?.getBoundingBox(index) ?: Rect.Zero

    fun measureComponent(string: String, style: TextStyle): TextLayoutResult {
        return textMeasurer.measure(string, style).also { componentTextLayoutResult = it }
    }

    fun measure(string: String, style: TextStyle): TextLayoutResult {
        return digitsTextLayoutResultCache[string]?.get() ?: textMeasurer.measure(string, style).also {
            digitsTextLayoutResultCache[string] = WeakReference(it)
        }
    }

    fun animateTo(text: String, animationSpec: AnimationSpec<Float>) {
        // this will enqueue the pending next animation,
        // this will avoid canceling the animation or make weird jumps when the values changes fast
        enqueuedNextVisibleText = text

        if (isAnimating) {
            return
        }

        coroutineScope.launch {
            isAnimating = true
            if (!enqueuedNextVisibleText.isNullOrEmpty()) {
                internalAnimateTo(enqueuedNextVisibleText.orEmpty(), animationSpec)
            }
            isAnimating = false
        }
    }

    private suspend fun internalAnimateTo(text: String, animationSpec: AnimationSpec<Float>) {
        _targetText = text
        animatable.snapTo(0f)
        animatable.animateTo(1f, animationSpec)
        _visibleText = text

        if (!enqueuedNextVisibleText.isNullOrEmpty()) {
            val nextText = enqueuedNextVisibleText.orEmpty()
            enqueuedNextVisibleText = null
            internalAnimateTo(nextText, animationSpec)
        }
    }
}