package com.franksap2.finances.ui.stockdetail.components.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.franksap2.finances.ui.stockdetail.StockInfoUi

@Composable
fun StockDetailHeader(
    infoState: StockInfoUi,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {

            Text(
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                text = infoState.stockProfile.companyName,
                color = MaterialTheme.colors.onBackground
            )

            Text(
                style = MaterialTheme.typography.body1,
                text = infoState.stockProfile.ticker,
                color = MaterialTheme.colors.onBackground
            )
        }

        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(52.dp),
            model = infoState.stockProfile.logo,
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}