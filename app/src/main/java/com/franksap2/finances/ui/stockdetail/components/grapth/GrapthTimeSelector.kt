package com.franksap2.finances.ui.stockdetail.components.grapth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.franksap2.finances.ui.stockdetail.StockDetailIntent
import com.franksap2.finances.ui.stockdetail.StockDetailUiState
import com.franksap2.finances.ui.stockdetail.TimeSelectorUi
import com.franksap2.finances.ui.theme.negative
import com.franksap2.finances.ui.theme.positive

@Composable
internal fun GraphTimeSelector(
    state: StockDetailUiState.Content,
    sendIntent: (StockDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(modifier = modifier.fillMaxWidth()) {

        TimeSelectorUi.entries.fastForEach { item ->

            val isSelected = state.stockPriceInfo.stockChartData.timeSelector == item
            val textColor by animateColorAsState(
                when {
                    isSelected && state.stockPriceInfo.isNegative -> MaterialTheme.colors.negative
                    isSelected -> MaterialTheme.colors.positive
                    else -> MaterialTheme.colors.onSurface
                }
            )

            val textTargetColor = animateColorAsState(textColor, tween(400))

            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { sendIntent(StockDetailIntent.TimeSelected(item)) }
                    ),
                text = stringResource(id = item.text),
                color = textTargetColor.value,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
        }
    }
}