package com.astro.vajra.di

import android.content.Context
import androidx.room.Room
import com.astro.vajra.data.local.ChartDao
import com.astro.vajra.data.local.ChartDatabase
import com.astro.vajra.data.localization.LocalizationManager
import com.astro.vajra.data.preferences.AstrologySettingsManager
import com.astro.vajra.data.preferences.OnboardingManager
import com.astro.vajra.data.preferences.ThemeManager
import com.astro.vajra.ephemeris.GocharaVedhaCalculator
import com.astro.vajra.ephemeris.HoroscopeCalculator
import com.astro.vajra.ephemeris.SwissEphemerisEngine
import com.astro.vajra.ephemeris.TarabalaCalculator
import com.astro.vajra.ephemeris.TransitAnalyzer
import com.astro.vajra.util.ChartExporter
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
    fun provideSwissEphemerisEngine(
        @ApplicationContext context: Context,
        astrologySettingsManager: AstrologySettingsManager
    ): SwissEphemerisEngine {
        // Initialize the singleton instance
        return SwissEphemerisEngine.getInstance(context, astroSettings = astrologySettingsManager)
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

/**
 * Entry point for common core dependencies
 */
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
@dagger.hilt.EntryPoint
interface CoreEntryPoint {
    fun ephemerisEngine(): SwissEphemerisEngine
    fun localizationManager(): LocalizationManager
    fun astrologySettingsManager(): AstrologySettingsManager
    fun themeManager(): ThemeManager
    fun onboardingManager(): OnboardingManager
    fun templateSelector(): com.astro.vajra.data.templates.TemplateSelector
}
