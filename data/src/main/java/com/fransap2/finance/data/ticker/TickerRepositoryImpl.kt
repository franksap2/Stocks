package com.fransap2.finance.data.ticker

import com.franksap2.finance.datasource.MockLocalDataSource
import com.franksap2.finance.domain.model.Stock
import com.franksap2.finance.domain.model.TimeSelector
import com.franksap2.finance.domain.repositories.TickerRepository
import javax.inject.Inject

internal class TickerRepositoryImpl @Inject constructor(
    private val mockLocalDataSource: MockLocalDataSource,
) : TickerRepository {


    override suspend fun getHistoricCandle(timeSelector: TimeSelector): Result<Stock> = runCatching {
        mockLocalDataSource.getStockChartData(timeSelector.toDto()).toDomain()
    }

}
