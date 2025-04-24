package com.franksap2.finances.ui.stockdetail.components.details

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.franksap2.finances.R
import com.franksap2.finances.ui.stockdetail.AverageStockRecommendationUi
import com.franksap2.finances.ui.theme.negative
import com.franksap2.finances.ui.theme.positive
import com.franksap2.finances.utils.formatToPercent

@Composable
internal fun StockRecommendation(
    recommendation: AverageStockRecommendationUi,
    isNegative: Boolean,
    modifier: Modifier = Modifier,
) {

    StockDetailContainer(
        modifier = modifier,
        title = stringResource(id = R.string.analytis_title)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            TextCircleBackground(
                data = recommendation,
                textColor = animateColorAsState(
                    if (isNegative) MaterialTheme.colors.negative else MaterialTheme.colors.positive
                ).value,
            )

            Column(
                modifier = Modifier.defaultMinSize(minHeight = 90.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                PercentProgressBar(
                    percent = recommendation.buyRating,
                    text = recommendation.buyRatingFormatted,
                    color = MaterialTheme.colors.positive,
                    legendText = stringResource(id = R.string.buy)
                )

                PercentProgressBar(
                    percent = recommendation.holdRating,
                    text = recommendation.holdRatingFormatted,
                    color = MaterialTheme.colors.onBackground,
                    legendText = stringResource(id = R.string.hold)
                )

                PercentProgressBar(
                    percent = recommendation.sellRating,
                    text = recommendation.sellRatingFormatted,
                    color = MaterialTheme.colors.negative,
                    legendText = stringResource(id = R.string.sell)
                )
            }
        }
    }


}

@Composable
internal fun TextCircleBackground(
    data: AverageStockRecommendationUi,
    textColor: Color,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .defaultMinSize(100.dp, 100.dp)
            .padding(8.dp)
            .background(textColor.copy(0.2f), CircleShape)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(data.buyRating.formatToPercent(decimals = 0))
                }

                withStyle(
                    SpanStyle(
                        fontSize = MaterialTheme.typography.caption.fontSize,
                        color = textColor
                    )
                ) {
                    append(
                        stringResource(R.string.analyst_rating, data.totalRatings)
                    )
                }
            }
        )
    }
}