package com.astro.storm.core.model

import swisseph.SweConst

enum class NodeType(val swissEphId: Int) {
    MEAN(SweConst.SE_MEAN_NODE),
    TRUE(SweConst.SE_TRUE_NODE)
}

enum class Ayanamsa(val swissEphId: Int) {
    LAHIRI(SweConst.SE_SIDM_LAHIRI),
    RAMAN(SweConst.SE_SIDM_RAMAN),
    KRISHNAMURTI(SweConst.SE_SIDM_KP),
    FAGAN_BRADLEY(SweConst.SE_SIDM_FAGAN_BRADLEY),
    YUKTESHWAR(SweConst.SE_SIDM_YUKTESHWAR),
    JN_BHASIN(SweConst.SE_SIDM_JN_BHASIN),
    SAYANA(SweConst.SE_SIDM_NONE); // Tropical

    companion object {
        fun fromSwissEphId(id: Int): Ayanamsa {
            return entries.find { it.swissEphId == id } ?: LAHIRI
        }
    }
}

data class CalculationSettings(
    val nodeType: NodeType = NodeType.MEAN,
    val ayanamsa: Ayanamsa = Ayanamsa.LAHIRI,
    val houseSystem: HouseSystem = HouseSystem.PLACIDUS
)
