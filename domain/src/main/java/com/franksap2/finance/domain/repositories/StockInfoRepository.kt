package com.franksap2.finance.domain.repositories

import com.franksap2.finance.domain.model.StockInfo

interface StockInfoRepository {

    suspend fun getStockInfo(): Result<StockInfo>

}