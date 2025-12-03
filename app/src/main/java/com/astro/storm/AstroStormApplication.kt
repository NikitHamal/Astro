package com.astro.storm

import android.app.Application
import android.util.Log
import com.astro.storm.ephemeris.SwissEphemerisEngine
import com.astro.storm.util.GlobalExceptionHandler
import java.io.IOException

/**
 * Application class for AstroStorm
 */
class AstroStormApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize the global exception handler
        GlobalExceptionHandler.initialize(this)

        // Copy ephemeris files once on app startup
        try {
            SwissEphemerisEngine(this).copyEphemerisFiles(this)
        } catch (e: IOException) {
            Log.e("AstroStormApplication", "Failed to copy ephemeris files", e)
        }
    }
}
