package com.franksap2.finances.ui.stockdetail.components.grapth.candle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.franksap2.finances.R
import com.franksap2.finances.ui.stockdetail.CandleUi
import com.franksap2.finances.ui.stockdetail.components.grapth.GraphState
import com.franksap2.finances.ui.theme.negative
import com.franksap2.finances.ui.theme.onSurfaceVariant
import com.franksap2.finances.ui.theme.positive
import com.franksap2.finances.ui.theme.surfaceVariant
import com.franksap2.finances.utils.formatToCurrency
import com.franksap2.finances.utils.formatToPercent

private val candleDetailBoxPadding = 8.dp

@Composable
internal fun CandleDetailBox(
    currentChartDataSelected: CandleUi?,
    graphState: GraphState,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = currentChartDataSelected != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

            var cardWidth by remember { mutableIntStateOf(0) }
            val maxPosition = constraints.maxWidth - cardWidth

            val candleDetailBoxPaddingPx = with(LocalDensity.current) { candleDetailBoxPadding.toPx() }

            val adjustedPosition by animateFloatAsState(
                if (graphState.selectedCandleCenterX >= maxPosition) {
                    -cardWidth.toFloat() - candleDetailBoxPaddingPx
                } else {
                    candleDetailBoxPaddingPx
                }
            )

            val gainsTextColor by animateColorAsState(
                if (currentChartDataSelected?.isNegative == true) {
                    MaterialTheme.colors.negative
                } else {
                    MaterialTheme.colors.positive
                }
            )

            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .onSizeChanged {
                        if (currentChartDataSelected != null) {
                            cardWidth = it.width
                        }
                    }
                    .graphicsLayer {
                        translationY = graphState.textIndicatorHeight
                        translationX = (graphState.selectedCandleCenterX + adjustedPosition).coerceAtLeast(0f)
                    }
                    .background(MaterialTheme.colors.surfaceVariant, MaterialTheme.shapes.medium)
                    .padding(8.dp),
            ) {

                currentChartDataSelected?.let {
                    CandleTextDetail(
                        title = stringResource(R.string.open_price),
                        subTitle = currentChartDataSelected.openPrice.formatToCurrency(),
                    )
                    CandleTextDetail(
                        title = stringResource(R.string.close_price),
                        subTitle = currentChartDataSelected.closedPrice.formatToCurrency(),
                    )
                    CandleTextDetail(
                        title = stringResource(R.string.high_price),
                        subTitle = currentChartDataSelected.highestPrice.formatToCurrency(),
                    )
                    CandleTextDetail(
                        title = stringResource(R.string.low_price),
                        subTitle = currentChartDataSelected.lowestPrice.formatToCurrency(),
                    )
                    CandleTextDetail(
                        title = stringResource(R.string.candle_gain),
                        subTitle = currentChartDataSelected.gains.formatToPercent(),
                        color = gainsTextColor
                    )
                }
            }
        }
    }
}

@Composable
private fun CandleTextDetail(
    title: String,
    subTitle: String,
    color: Color = MaterialTheme.colors.onSurfaceVariant,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.onSurfaceVariant
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = subTitle,
            color = color
        )
    }
}