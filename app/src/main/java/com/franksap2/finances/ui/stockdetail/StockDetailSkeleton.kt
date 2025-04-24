package com.franksap2.finances.ui.stockdetail

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.franksap2.finances.ui.components.Shimmer
import com.franksap2.finances.ui.components.shimmer
import com.franksap2.finances.ui.stockdetail.components.grapth.utils.infiniteRevealMask
import com.franksap2.finances.ui.stockdetail.components.grapth.utils.nextFloat
import com.franksap2.finances.ui.theme.surfaceVariant
import kotlin.random.Random

@Composable
internal fun StockDetailSkeleton(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {

        HeaderSkeleton()

        GraphSkeleton()

        Row(
            modifier = Modifier.shimmer(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(TimeSelectorUi.entries.size) {
                Shimmer(
                    modifier = Modifier
                        .size(32.dp),
                    shape = MaterialTheme.shapes.medium
                )
            }
        }

        RecommendationSkeleton(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun HeaderSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Shimmer(modifier = Modifier.size(108.dp, 28.dp))
                Shimmer(modifier = Modifier.size(56.dp, 20.dp))
            }

            Shimmer(modifier = Modifier.size(52.dp), shape = CircleShape)
        }

        Shimmer(modifier = Modifier.size(120.dp, 36.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Shimmer(modifier = Modifier.size(64.dp, 20.dp))
            Shimmer(modifier = Modifier.size(64.dp, 20.dp))

            Spacer(modifier = Modifier.weight(1f))

            Shimmer(modifier = Modifier.size(24.dp), shape = CircleShape)
        }

    }
}

@Composable
private fun GraphSkeleton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surfaceVariant,
) {

    val path = remember { Path() }
    val revealProgress by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        val maxPeekHeight = 32.dp.toPx()

        if (path.isEmpty) {

            val peeks = 20
            val steps = size.width / peeks
            val increaseArea = size.height * 0.1f

            path.moveTo(0f, (size.height + increaseArea) / 2)

            repeat(peeks) {
                val upPercent = Random.nextFloat(min = -0.8f, max = 0.8f)

                val upProgress = (increaseArea / 2) * it / 10f

                val y = size.height / 2 - (maxPeekHeight * upPercent)

                path.lineTo(it.inc() * steps, y - upProgress)
            }
        }

        infiniteRevealMask(
            progressProvider = { revealProgress },
            hideMaskOffset = size.width * 0.8f,
        ) {
            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 6.dp.toPx(),
                    pathEffect = PathEffect.cornerPathEffect(maxPeekHeight / 2f)
                ),
            )
        }
    }

}

@Composable
private fun RecommendationSkeleton(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shimmer()
    ) {

        Shimmer(modifier = Modifier.size(120.dp, 36.dp))

        Divider(modifier = Modifier.padding(vertical = 12.dp))

        Shimmer(
            modifier = Modifier
                .fillMaxWidth()
                .height(800.dp)
                .shimmer(),
            shape = MaterialTheme.shapes.large
        )

    }

}