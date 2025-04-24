package com.franksap2.finance.domain.usecases

import com.franksap2.finance.domain.model.StockInfo
import com.franksap2.finance.domain.repositories.StockInfoRepository
import javax.inject.Inject

class GetStockInfoUseCase @Inject constructor(
    private val stockInfoRepository: StockInfoRepository,
) {

    suspend operator fun invoke(): Result<StockInfo> {
        return stockInfoRepository.getStockInfo()
    }
}