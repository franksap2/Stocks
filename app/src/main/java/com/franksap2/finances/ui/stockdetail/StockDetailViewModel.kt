package com.franksap2.finances.ui.stockdetail


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franksap2.finance.domain.model.StockChartData
import com.franksap2.finance.domain.model.StockInfo
import com.franksap2.finance.domain.model.TimeSelector
import com.franksap2.finance.domain.usecases.GetChartDataUseCase
import com.franksap2.finance.domain.usecases.GetStockInfoUseCase
import com.franksap2.finances.di.qualifiers.IODispatcher
import com.franksap2.finances.ui.stockdetail.mappers.toDomain
import com.franksap2.finances.ui.stockdetail.mappers.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockDetailViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val getChartDataUseCase: GetChartDataUseCase,
    private val getStockInfoUseCase: GetStockInfoUseCase,
) : ViewModel() {

    private val stockChartData = MutableStateFlow<StockChartData?>(null)
    private val stockInfo = MutableStateFlow<StockInfo?>(null)
    private val currentChartSelection = MutableStateFlow<CandleUi?>(null)
    private val chartType = MutableStateFlow(ChartType.LINE)

    private var stockChartDataJob: Job? = null

    val state: StateFlow<StockDetailUiState> = combine(
        stockChartData,
        stockInfo,
        currentChartSelection,
        chartType
    ) { stockChart, stockInfo, currentChartSelection, chartType ->
        if (stockChart == null || stockInfo == null) {
            return@combine StockDetailUiState.Loading
        } else {
            StockDetailUiState.Content(
                stockInfo = stockInfo.toUi(),
                currentChartSelection = currentChartSelection,
                stockPriceInfo = stockChart.toUi(chartType = chartType, currentChartSelection = currentChartSelection)
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StockDetailUiState.Loading
    )

    fun init() {
        requestStockInfo()
        requestStockCharData(TimeSelector.DAY)
    }

    fun onIntent(intent: StockDetailIntent) {
        when (intent) {
            is StockDetailIntent.CurrentSelection -> currentChartSelection.value = intent.charData
            is StockDetailIntent.ClearSelection -> currentChartSelection.value = null
            is StockDetailIntent.TimeSelected -> requestStockCharData(intent.timeSelector.toDomain())
            StockDetailIntent.ChangeToCandleGraph -> chartType.value = ChartType.CANDLE
            StockDetailIntent.ChangeToLineGraph -> chartType.value = ChartType.LINE
        }
    }

    private fun requestStockInfo() {
        viewModelScope.launch(dispatcher) {
            getStockInfoUseCase().fold(
                onSuccess = { stockInfo.value = it },
                onFailure = {
                    Log.e("StockDetailViewModel", "Error fetching stock info", it)
                }
            )
        }
    }

    private fun requestStockCharData(timeSelector: TimeSelector) {
        stockChartDataJob?.cancel()
        stockChartDataJob = viewModelScope.launch(dispatcher) {
            getChartDataUseCase(timeSelector).fold(
                onSuccess = { stockChartData.value = it },
                onFailure = {

                }
            )
        }
    }
}




