package com.franksap2.finance.datasource

import com.franksap2.finance.datasource.dtos.TimeSelectorDto
import com.franksap2.finance.datasource.model.StockInfoResponse
import com.franksap2.finance.datasource.model.StockResponse

interface MockLocalDataSource {

    suspend fun getStockInfo(): StockInfoResponse

    suspend fun getStockChartData(timeSelector: TimeSelectorDto): StockResponse

}