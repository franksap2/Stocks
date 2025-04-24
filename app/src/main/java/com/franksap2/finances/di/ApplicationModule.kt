package com.franksap2.finances.di

import com.franksap2.finances.di.qualifiers.IODispatcher
import com.franksap2.finances.di.qualifiers.UiDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @IODispatcher
    @Provides
    fun provideIODispatcher() = Dispatchers.IO

    @UiDispatcher
    @Provides
    fun provideMainDispatcher() = Dispatchers.Main

}