package com.astro.storm.di

import android.content.Context
import androidx.room.Room
import com.astro.storm.data.local.ChartDao
import com.astro.storm.data.local.ChartDatabase
import com.astro.storm.data.local.chat.ChatDao
import com.astro.storm.ephemeris.SwissEphemerisEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideChartDatabase(
        @ApplicationContext context: Context
    ): ChartDatabase {
        return ChartDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideChartDao(database: ChartDatabase): ChartDao {
        return database.chartDao()
    }

    @Provides
    @Singleton
    fun provideChatDao(database: ChartDatabase): ChatDao {
        return database.chatDao()
    }

    @Provides
    @Singleton
    fun provideSwissEphemerisEngine(
        @ApplicationContext context: Context
    ): SwissEphemerisEngine {
        // Initialize the singleton instance
        return SwissEphemerisEngine.getInstance(context)
    }

    @Provides
    @Singleton
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
