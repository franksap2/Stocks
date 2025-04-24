package com.fransap2.finance.data.di

import com.franksap2.finance.domain.repositories.StockInfoRepository
import com.franksap2.finance.domain.repositories.TickerRepository
import com.fransap2.finance.data.info.StockInfoInfoRepositoryImpl
import com.fransap2.finance.data.ticker.TickerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    internal abstract fun stockRepository(
        stockInfoRepository: StockInfoInfoRepositoryImpl,
    ): StockInfoRepository

    @Binds
    internal abstract fun tickerRepository(
        tickerRepository: TickerRepositoryImpl,
    ): TickerRepository


}
