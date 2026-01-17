package com.astro.storm.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.BINARY

@Qualifier
@Retention(BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(BINARY)
annotation class DefaultDispatcher
