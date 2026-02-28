package com.astro.vajra.core.model

import swisseph.SweConst

enum class NodeType(val swissEphId: Int) {
    MEAN(SweConst.SE_MEAN_NODE),
    TRUE(SweConst.SE_TRUE_NODE)
}

enum class Ayanamsa(val swissEphId: Int) {
    LAHIRI(SweConst.SE_SIDM_LAHIRI),
    RAMAN(SweConst.SE_SIDM_RAMAN),
    KRISHNAMURTI(SweConst.SE_SIDM_KRISHNAMURTI),
    FAGAN_BRADLEY(SweConst.SE_SIDM_FAGAN_BRADLEY),
    YUKTESHWAR(SweConst.SE_SIDM_YUKTESHWAR),
    JN_BHASIN(SweConst.SE_SIDM_JN_BHASIN),
    TRUE_CHITRAPAKSHA(SweConst.SE_SIDM_TRUE_CITRA),
    SAYANA(-1); // Tropical - use literal -1 since no constant exists in SweConst

    companion object {
        fun fromSwissEphId(id: Int): Ayanamsa {
            return entries.find { it.swissEphId == id } ?: LAHIRI
        }
    }
}

data class CalculationSettings(
    val nodeType: NodeType = NodeType.MEAN,
    val ayanamsa: Ayanamsa = Ayanamsa.LAHIRI,
    val houseSystem: HouseSystem = HouseSystem.DEFAULT
)
