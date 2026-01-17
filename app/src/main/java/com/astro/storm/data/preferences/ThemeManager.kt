package com.astro.storm.data.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Theme Manager for persisting and managing theme preferences
 *
 * Supports:
 * - Light mode
 * - Dark mode
 * - System default (follows device settings)
 *
 * Theme preference is persisted using SharedPreferences for
 * instant access on app startup without database overhead.
 */
@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _themeMode = MutableStateFlow(getPersistedTheme())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    /**
     * Set the theme mode and persist it
     */
    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
        _themeMode.value = mode
    }

    /**
     * Get the persisted theme from SharedPreferences
     */
    private fun getPersistedTheme(): ThemeMode {
        val savedTheme = prefs.getString(KEY_THEME_MODE, ThemeMode.DARK.name)
        return try {
            ThemeMode.valueOf(savedTheme ?: ThemeMode.DARK.name)
        } catch (e: IllegalArgumentException) {
            ThemeMode.DARK // Default to dark mode for astrology app
        }
    }

    /**
     * Check if dark mode should be used based on current theme mode and system settings
     */
    fun isDarkMode(isSystemDarkTheme: Boolean): Boolean {
        return when (_themeMode.value) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemDarkTheme
        }
    }

    companion object {
        private const val PREFS_NAME = "astro_storm_theme_prefs"
        private const val KEY_THEME_MODE = "theme_mode"

        @Volatile
        private var instance: ThemeManager? = null

        fun getInstance(context: Context): ThemeManager =
            instance ?: synchronized(this) {
                instance ?: ThemeManager(context.applicationContext).also {
                    instance = it
                }
            }
    }
}

/**
 * Available theme modes
 */
enum class ThemeMode(val displayName: String, val description: String) {
    LIGHT("Light", "Always use light theme"),
    DARK("Dark", "Always use dark theme"),
    SYSTEM("System", "Follow device settings")
}
