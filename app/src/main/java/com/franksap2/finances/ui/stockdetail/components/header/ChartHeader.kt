package com.franksap2.finances.ui.stockdetail.components.header

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.franksap2.finances.R
import com.franksap2.finances.ui.stockdetail.ChartType
import com.franksap2.finances.ui.stockdetail.StockDetailIntent
import com.franksap2.finances.ui.stockdetail.StockPriceInfoUi
import com.franksap2.finances.ui.theme.negative
import com.franksap2.finances.ui.theme.positive

@Composable
internal fun ChartHeader(
    stockChartData: StockPriceInfoUi,
    sendIntent: (StockDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val textColor by animateColorAsState(
        if (stockChartData.isNegative) MaterialTheme.colors.negative else MaterialTheme.colors.positive
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        PercentageGainText(
            stockChartData = stockChartData,
            color = textColor,
        )

        TickerText(
            color = textColor,
            text = stringResource(R.string.gains, stockChartData.gainsFormatted),
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.weight(1f))


        AnimatedContent(
            targetState = stockChartData.stockChartData.chartType,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
        ) {
            when (it) {
                ChartType.LINE -> ChartSelectorButton(
                    onClick = { sendIntent(StockDetailIntent.ChangeToCandleGraph) },
                    icon = painterResource(R.drawable.ic_candle_chart)
                )

                ChartType.CANDLE -> ChartSelectorButton(
                    onClick = { sendIntent(StockDetailIntent.ChangeToLineGraph) },
                    icon = painterResource(R.drawable.ic_line_chart)
                )
            }
        }
    }
}

@Composable
private fun PercentageGainText(
    stockChartData: StockPriceInfoUi,
    color: Color,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val iconRotation by animateFloatAsState(
            targetValue = if (stockChartData.percentGains > 0) 180f else 0f
        )

        Icon(
            modifier = Modifier
                .size(24.dp)
                .graphicsLayer {
                    rotationX = iconRotation
                },
            painter = painterResource(R.drawable.ic_gain_arrow),
            contentDescription = null,
            tint = color
        )

        TickerText(
            modifier = Modifier,
            color = color,
            text = stockChartData.percentGainsFormatted,
            style = MaterialTheme.typography.body1
        )

    }

}

@Composable
private fun ChartSelectorButton(
    onClick: () -> Unit,
    icon: Painter,
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground
            )
        }
    )
}