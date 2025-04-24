package com.franksap2.finances.ui.stockdetail.components.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun StockDetailContainer(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = title,
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onBackground
        )

        Divider(modifier = Modifier.padding(vertical = 12.dp))

        content()
    }

}
