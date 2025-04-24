package com.franksap2.finance.datasource.di

import com.franksap2.finance.datasource.MockLocalDataSource
import com.franksap2.finance.datasource.MockLocalDataSourceImpl
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    internal abstract fun bindMockLocalDataSource(mockLocalDataSource: MockLocalDataSourceImpl): MockLocalDataSource

}

@Module
@InstallIn(SingletonComponent::class)
object SerializerModule {

    @Singleton
    @Provides
    internal fun provideGson(): Gson {
        return Gson()
    }

}