package com.franksap2.finances.ui.stockdetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.franksap2.finances.ui.stockdetail.components.StockDetailContent

@Composable
fun FinanceApp(
    viewModel: StockDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) { viewModel.init() }

    StockDetailScreen(
        state = state,
        sendIntent = viewModel::onIntent
    )
}

@Composable
private fun StockDetailScreen(
    state: StockDetailUiState,
    sendIntent: (StockDetailIntent) -> Unit,
) {

    AnimatedContent(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        targetState = state,
        contentKey = { it.toContentKey() },
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) {
        when (it) {
            is StockDetailUiState.Loading -> StockDetailSkeleton()
            is StockDetailUiState.Content -> StockDetailContent(state = it, sendIntent = sendIntent)
        }
    }
}
