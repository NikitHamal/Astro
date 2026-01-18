package com.astro.storm

import android.app.Application
import android.util.Log
import com.astro.storm.data.localization.LocalizationManager
import com.astro.storm.ephemeris.SwissEphemerisEngine
import com.astro.storm.util.GlobalExceptionHandler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class for AstroStorm
 */
@HiltAndroidApp
class AstroStormApplication : Application() {

    @Inject
    lateinit var ephemerisEngine: SwissEphemerisEngine

    @Inject
    lateinit var localizationManager: LocalizationManager

    override fun onCreate() {
        super.onCreate()
        // Initialize the global exception handler
        GlobalExceptionHandler.initialize(this)

        // Hilt will automatically inject ephemerisEngine and localizationManager here
        // No manual initialization needed as they are provided by Hilt as singletons
        // migrateOldPreferences is already called by LocalizationManager if needed or we can call it here
        try {
            localizationManager.migrateOldPreferences()
            Log.i("AstroStormApplication", "SwissEphemerisEngine initialized: $ephemerisEngine")
        } catch (e: Exception) {
            Log.e("AstroStormApplication", "Error during app initialization", e)
        }
    }
}
