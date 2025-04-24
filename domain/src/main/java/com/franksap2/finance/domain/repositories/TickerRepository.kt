package com.franksap2.finance.domain.repositories

import com.franksap2.finance.domain.model.Stock
import com.franksap2.finance.domain.model.TimeSelector

interface TickerRepository {

    suspend fun getHistoricCandle(timeSelector: TimeSelector): Result<Stock>

}