package com.astro.vajra.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.astro.vajra.core.model.Ayanamsa
import com.astro.vajra.core.model.HouseSystem
import com.astro.vajra.ephemeris.AstrologicalConstants
import com.astro.vajra.ephemeris.DashaUtils
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
 * Dasha timing basis.
 *
 * SAVANA_360 follows classical 360-day dasha year usage.
 * TROPICAL_365 keeps modern tropical-year precision.
 */
enum class DashaYearBasis {
    SAVANA_360,
    TROPICAL_365_24219
}

/**
 * Ashtamangala shell generation mode.
 */
enum class AshtamangalaMode {
    DETERMINISTIC_DAILY,
    CLASSIC_RANDOM
}

/**
 * Outer planet usage policy in classical rule engines.
 */
enum class OuterPlanetMode {
    CLASSICAL_ONLY,
    INCLUDE_IN_EXTENDED
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

    private val _dashaYearBasis = MutableStateFlow(getPersistedDashaYearBasis())
    val dashaYearBasis: StateFlow<DashaYearBasis> = _dashaYearBasis.asStateFlow()

    private val _ashtamangalaMode = MutableStateFlow(getPersistedAshtamangalaMode())
    val ashtamangalaMode: StateFlow<AshtamangalaMode> = _ashtamangalaMode.asStateFlow()

    private val _outerPlanetMode = MutableStateFlow(getPersistedOuterPlanetMode())
    val outerPlanetMode: StateFlow<OuterPlanetMode> = _outerPlanetMode.asStateFlow()

    init {
        DashaUtils.setDefaultYearBasis(_dashaYearBasis.value.toDashaUtilsBasis())
        AstrologicalConstants.setOuterPlanetMode(_outerPlanetMode.value.toAstroOuterMode())
    }

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
     * Update dasha year basis.
     */
    fun setDashaYearBasis(value: DashaYearBasis) {
        prefs.edit().putString(KEY_DASHA_YEAR_BASIS, value.name).apply()
        _dashaYearBasis.value = value
        DashaUtils.setDefaultYearBasis(value.toDashaUtilsBasis())
    }

    /**
     * Update Ashtamangala generation mode.
     */
    fun setAshtamangalaMode(value: AshtamangalaMode) {
        prefs.edit().putString(KEY_ASHTAMANGALA_MODE, value.name).apply()
        _ashtamangalaMode.value = value
    }

    /**
     * Update outer-planet policy.
     */
    fun setOuterPlanetMode(value: OuterPlanetMode) {
        prefs.edit().putString(KEY_OUTER_PLANET_MODE, value.name).apply()
        _outerPlanetMode.value = value
        AstrologicalConstants.setOuterPlanetMode(value.toAstroOuterMode())
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
        val name = prefs.getString(KEY_HOUSE_SYSTEM, HouseSystem.DEFAULT.name)
        return try {
            HouseSystem.valueOf(name ?: HouseSystem.DEFAULT.name)
        } catch (e: Exception) {
            HouseSystem.DEFAULT
        }
    }

    private fun getPersistedDashaYearBasis(): DashaYearBasis {
        val name = prefs.getString(KEY_DASHA_YEAR_BASIS, DashaYearBasis.SAVANA_360.name)
        return try {
            DashaYearBasis.valueOf(name ?: DashaYearBasis.SAVANA_360.name)
        } catch (e: Exception) {
            DashaYearBasis.SAVANA_360
        }
    }

    private fun getPersistedAshtamangalaMode(): AshtamangalaMode {
        val name = prefs.getString(KEY_ASHTAMANGALA_MODE, AshtamangalaMode.DETERMINISTIC_DAILY.name)
        return try {
            AshtamangalaMode.valueOf(name ?: AshtamangalaMode.DETERMINISTIC_DAILY.name)
        } catch (e: Exception) {
            AshtamangalaMode.DETERMINISTIC_DAILY
        }
    }

    private fun DashaYearBasis.toDashaUtilsBasis(): DashaUtils.DashaYearBasis {
        return when (this) {
            DashaYearBasis.SAVANA_360 -> DashaUtils.DashaYearBasis.SAVANA_360
            DashaYearBasis.TROPICAL_365_24219 -> DashaUtils.DashaYearBasis.TROPICAL_365_24219
        }
    }

    private fun getPersistedOuterPlanetMode(): OuterPlanetMode {
        val name = prefs.getString(KEY_OUTER_PLANET_MODE, OuterPlanetMode.CLASSICAL_ONLY.name)
        return try {
            OuterPlanetMode.valueOf(name ?: OuterPlanetMode.CLASSICAL_ONLY.name)
        } catch (e: Exception) {
            OuterPlanetMode.CLASSICAL_ONLY
        }
    }

    private fun OuterPlanetMode.toAstroOuterMode(): AstrologicalConstants.OuterPlanetMode {
        return when (this) {
            OuterPlanetMode.CLASSICAL_ONLY -> AstrologicalConstants.OuterPlanetMode.CLASSICAL_ONLY
            OuterPlanetMode.INCLUDE_IN_EXTENDED -> AstrologicalConstants.OuterPlanetMode.INCLUDE_IN_EXTENDED
        }
    }

    companion object {
        private const val PREFS_NAME = "astro_storm_astrology_settings"
        private const val KEY_NODE_MODE = "node_mode"
        private const val KEY_AYANAMSA = "ayanamsa"
        private const val KEY_HOUSE_SYSTEM = "house_system"
        private const val KEY_DASHA_YEAR_BASIS = "dasha_year_basis"
        private const val KEY_ASHTAMANGALA_MODE = "ashtamangala_mode"
        private const val KEY_OUTER_PLANET_MODE = "outer_planet_mode"

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
