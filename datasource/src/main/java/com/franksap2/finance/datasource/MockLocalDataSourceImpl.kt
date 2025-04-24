package com.franksap2.finance.datasource

import com.franksap2.finance.datasource.dtos.TimeSelectorDto
import com.franksap2.finance.datasource.mocks.chart.fiveYearJson
import com.franksap2.finance.datasource.mocks.chart.monthJson
import com.franksap2.finance.datasource.mocks.chart.sameDayJson
import com.franksap2.finance.datasource.mocks.chart.threeMonthJson
import com.franksap2.finance.datasource.mocks.chart.weekJson
import com.franksap2.finance.datasource.mocks.chart.yearJson
import com.franksap2.finance.datasource.mocks.profile.stockInfoJson
import com.franksap2.finance.datasource.model.StockInfoResponse
import com.franksap2.finance.datasource.model.StockResponse
import com.google.gson.Gson
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class MockLocalDataSourceImpl @Inject constructor(
    private val gson: Gson,
) : MockLocalDataSource {

    override suspend fun getStockInfo(): StockInfoResponse {
        // Simulate a network delay
        delay(2_000)
        return gson.fromJson(stockInfoJson, StockInfoResponse::class.java)
    }

    override suspend fun getStockChartData(timeSelector: TimeSelectorDto): StockResponse {

        val json = when (timeSelector) {
            TimeSelectorDto.DAY -> sameDayJson
            TimeSelectorDto.WEEK -> weekJson
            TimeSelectorDto.MONTH -> monthJson
            TimeSelectorDto.THREE_MONTHS -> threeMonthJson
            TimeSelectorDto.YEAR -> yearJson
            TimeSelectorDto.FIVE_YEARS -> fiveYearJson
        }

        return gson.fromJson(json, StockResponse::class.java)

    }

}