package com.franksap2.finances.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.franksap2.finances.ui.stockdetail.FinanceApp
import com.franksap2.finances.ui.theme.FinancesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            FinancesTheme {
                FinanceApp()
            }
        }
    }

}
