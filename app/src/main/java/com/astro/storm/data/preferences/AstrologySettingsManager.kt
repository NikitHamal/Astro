package com.astro.storm.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Node calculation mode
 */
enum class NodeCalculationMode {
    MEAN,
    TRUE
}

/**
 * Astrology Settings Manager for persisting calculation preferences
 */
class AstrologySettingsManager private constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _nodeMode = MutableStateFlow(getPersistedNodeMode())
    val nodeMode: StateFlow<NodeCalculationMode> = _nodeMode.asStateFlow()

    /**
     * Update node calculation mode
     */
    fun setNodeMode(mode: NodeCalculationMode) {
        prefs.edit().putString(KEY_NODE_MODE, mode.name).apply()
        _nodeMode.value = mode
    }

    /**
     * Get persisted node mode
     */
    private fun getPersistedNodeMode(): NodeCalculationMode {
        val name = prefs.getString(KEY_NODE_MODE, NodeCalculationMode.MEAN.name)
        return try {
            NodeCalculationMode.valueOf(name ?: NodeCalculationMode.MEAN.name)
        } catch (e: Exception) {
            NodeCalculationMode.MEAN
        }
    }

    companion object {
        private const val PREFS_NAME = "astro_storm_astrology_settings"
        private const val KEY_NODE_MODE = "node_mode"

        @Volatile
        private var INSTANCE: AstrologySettingsManager? = null

        fun getInstance(context: Context): AstrologySettingsManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AstrologySettingsManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
}
