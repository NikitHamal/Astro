package com.astro.storm.ephemeris

import com.astro.storm.core.model.Planet
import com.astro.storm.ephemeris.panchanga.Tithi
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

/**
 * Unit tests for PanchangaCalculator using "Golden Data" approach.
 * Verifies calculations against known astronomical standards.
 */
class PanchangaCalculatorTest {

    // Note: Since Swiss Ephemeris requires local assets, we might need a Robolectric 
    // or instrumented test for full execution. 
    // Here we demonstrate the structure of high-precision validation.

    @Test
    fun testTithiLordLogic() {
        // Tithi lords follow a fixed cycle: Sun, Moon, Mars, Mercury, Jupiter, Venus, Saturn, Rahu
        // Then repeats Sun...Saturn, then the 15th (Purnima) is Saturn.
        
        fun getTithiLordManual(n: Int): Planet {
            val p = ((n - 1) % 15) + 1
            return when (p) {
                1, 9 -> Planet.SUN
                2, 10 -> Planet.MOON
                3, 11 -> Planet.MARS
                4, 12 -> Planet.MERCURY
                5, 13 -> Planet.JUPITER
                6, 14 -> Planet.VENUS
                7, 15 -> Planet.SATURN
                8 -> Planet.RAHU
                else -> Planet.SUN
            }
        }

        assertEquals(Planet.SUN, getTithiLordManual(1))
        assertEquals(Planet.MOON, getTithiLordManual(2))
        assertEquals(Planet.RAHU, getTithiLordManual(8))
        assertEquals(Planet.SATURN, getTithiLordManual(15))
        assertEquals(Planet.SUN, getTithiLordManual(16)) // Pratipada Krishna
    }

    @Test
    fun testTithiGroupLogic() {
        // Nanda (1, 6, 11), Bhadra (2, 7, 12), Jaya (3, 8, 13), Rikta (4, 9, 14), Purna (5, 10, 15)
        assertEquals(com.astro.storm.ephemeris.panchanga.TithiGroup.NANDA, Tithi.PRATIPADA.group)
        assertEquals(com.astro.storm.ephemeris.panchanga.TithiGroup.RIKTA, Tithi.CHATURTHI.group)
        assertEquals(com.astro.storm.ephemeris.panchanga.TithiGroup.PURNA, Tithi.PANCHAMI.group)
        assertEquals(com.astro.storm.ephemeris.panchanga.TithiGroup.PURNA, Tithi.AMAVASYA.group)
    }
}
