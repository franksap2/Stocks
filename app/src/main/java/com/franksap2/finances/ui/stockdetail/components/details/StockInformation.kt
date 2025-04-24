package com.franksap2.finances.ui.stockdetail.components.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.franksap2.finances.R
import com.franksap2.finances.ui.stockdetail.StockInfoUi

@Composable
internal fun StockInformation(
    data: StockInfoUi,
    modifier: Modifier = Modifier,
) {
    StockDetailContainer(
        modifier = modifier,
        title = stringResource(id = R.string.stock_info, data.stockProfile.companyName)
    ) {
        Text(
            fontWeight = FontWeight.Light,
            text = data.description,
            color = MaterialTheme.colors.onBackground
        )

        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(id = R.string.company_ceo).uppercase(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onBackground
        )

        Text(
            fontWeight = FontWeight.Light,
            text = data.ceo,
            color = MaterialTheme.colors.onBackground
        )

    }
}
