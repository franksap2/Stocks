package com.franksap2.finances.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UiDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher
