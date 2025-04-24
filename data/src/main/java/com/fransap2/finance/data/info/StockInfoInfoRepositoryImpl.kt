package com.fransap2.finance.data.info

import com.franksap2.finance.datasource.MockLocalDataSource
import com.franksap2.finance.domain.model.StockInfo
import com.franksap2.finance.domain.repositories.StockInfoRepository
import javax.inject.Inject

internal class StockInfoInfoRepositoryImpl @Inject constructor(
    private val mockLocalDataSource: MockLocalDataSource,
) : StockInfoRepository {

    override suspend fun getStockInfo(): Result<StockInfo> = runCatching {
        mockLocalDataSource.getStockInfo().toDomain()
    }

}