package com.astro.storm.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.astro.storm.core.model.Ayanamsa
import com.astro.storm.core.model.HouseSystem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

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
@Singleton
class AstrologySettingsManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _nodeMode = MutableStateFlow(getPersistedNodeMode())
    val nodeMode: StateFlow<NodeCalculationMode> = _nodeMode.asStateFlow()

    private val _ayanamsa = MutableStateFlow(getPersistedAyanamsa())
    val ayanamsa: StateFlow<Ayanamsa> = _ayanamsa.asStateFlow()

    private val _houseSystem = MutableStateFlow(getPersistedHouseSystem())
    val houseSystem: StateFlow<HouseSystem> = _houseSystem.asStateFlow()

    /**
     * Update node calculation mode
     */
    fun setNodeMode(mode: NodeCalculationMode) {
        prefs.edit().putString(KEY_NODE_MODE, mode.name).apply()
        _nodeMode.value = mode
    }

    /**
     * Update ayanamsa
     */
    fun setAyanamsa(value: Ayanamsa) {
        prefs.edit().putString(KEY_AYANAMSA, value.name).apply()
        _ayanamsa.value = value
    }

    /**
     * Update house system
     */
    fun setHouseSystem(value: HouseSystem) {
        prefs.edit().putString(KEY_HOUSE_SYSTEM, value.name).apply()
        _houseSystem.value = value
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

    private fun getPersistedAyanamsa(): Ayanamsa {
        val name = prefs.getString(KEY_AYANAMSA, Ayanamsa.LAHIRI.name)
        return try {
            Ayanamsa.valueOf(name ?: Ayanamsa.LAHIRI.name)
        } catch (e: Exception) {
            Ayanamsa.LAHIRI
        }
    }

    private fun getPersistedHouseSystem(): HouseSystem {
        val name = prefs.getString(KEY_HOUSE_SYSTEM, HouseSystem.PLACIDUS.name)
        return try {
            HouseSystem.valueOf(name ?: HouseSystem.PLACIDUS.name)
        } catch (e: Exception) {
            HouseSystem.PLACIDUS
        }
    }

    companion object {
        private const val PREFS_NAME = "astro_storm_astrology_settings"
        private const val KEY_NODE_MODE = "node_mode"
        private const val KEY_AYANAMSA = "ayanamsa"
        private const val KEY_HOUSE_SYSTEM = "house_system"

        @Volatile
        private var instance: AstrologySettingsManager? = null

        fun getInstance(context: Context): AstrologySettingsManager =
            instance ?: synchronized(this) {
                instance ?: AstrologySettingsManager(context.applicationContext).also {
                    instance = it
                }
            }
    }
}