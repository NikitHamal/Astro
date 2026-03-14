package com.astro.vajra.ephemeris.skyview

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * Sidereal Sky Calculator
 *
 * Converts planetary positions from a VedicChart into local horizontal (altitude-azimuth)
 * coordinates for visualization of the sky as seen from the observer's location at the
 * observation time. This enables rendering a real-time or historical sky view overlaid
 * with Vedic nakshatra boundaries and house cusps.
 *
 * **Coordinate Transformation Pipeline:**
 * ```
 * Ecliptic (lambda, beta) -> Equatorial (RA, Dec) -> Horizontal (Alt, Az)
 * ```
 *
 * **Key Calculations:**
 * 1. Julian Day and century from date/time
 * 2. Greenwich Mean Sidereal Time (GMST) -> Local Sidereal Time (LST)
 * 3. Obliquity of the ecliptic (with nutation correction)
 * 4. Ecliptic-to-equatorial transformation
 * 5. Equatorial-to-horizontal transformation
 * 6. Rise, set, and culmination times for each planet
 * 7. Planetary visibility assessment (above horizon, solar glare proximity)
 * 8. Nakshatra boundary mapping to sky coordinates
 * 9. House cusp mapping to sky coordinates
 *
 * All calculations use double-precision IEEE 754 arithmetic with explicit
 * degrees-to-radians conversion. Formulas follow the IAU 2000 conventions
 * as simplified in Meeus' "Astronomical Algorithms" (2nd Ed., 1998).
 *
 * @author AstroVajra
 */
object SiderealSkyCalculator {

    // =========================================================================
    // CONSTANTS
    // =========================================================================

    /** Degrees to radians conversion factor */
    private const val DEG_TO_RAD = PI / 180.0

    /** Radians to degrees conversion factor */
    private const val RAD_TO_DEG = 180.0 / PI

    /** J2000.0 epoch Julian Day */
    private const val J2000_EPOCH = 2451545.0

    /** Julian days per Julian century */
    private const val DAYS_PER_CENTURY = 36525.0

    /** Standard atmospheric refraction at horizon (degrees) */
    private const val ATMOSPHERIC_REFRACTION = 0.5667

    /** Apparent radius of the Sun (degrees) for rise/set calculation */
    private const val SUN_APPARENT_RADIUS = 0.2666

    /**
     * Maximum elongation from the Sun (degrees) below which a planet is considered
     * in the Sun's glare and difficult to observe. Values per planet.
     */
    private val SOLAR_GLARE_LIMITS = mapOf(
        Planet.MERCURY to 10.0,
        Planet.VENUS to 5.0,
        Planet.MARS to 2.0,
        Planet.JUPITER to 2.0,
        Planet.SATURN to 2.0,
        Planet.MOON to 0.0,  // Moon is always visible (when above horizon)
        Planet.RAHU to 0.0,  // Nodes are mathematical points
        Planet.KETU to 0.0,
        Planet.URANUS to 2.0,
        Planet.NEPTUNE to 2.0,
        Planet.PLUTO to 2.0
    )

    /**
     * Approximate visual magnitude of planets at mean distance/phase.
     * Used for rough visibility assessment.
     */
    private val APPROXIMATE_MAGNITUDES = mapOf(
        Planet.SUN to -26.74,
        Planet.MOON to -12.7,
        Planet.MERCURY to -0.4,
        Planet.VENUS to -4.4,
        Planet.MARS to -2.0,
        Planet.JUPITER to -2.7,
        Planet.SATURN to 0.5,
        Planet.URANUS to 5.7,
        Planet.NEPTUNE to 7.8,
        Planet.PLUTO to 14.0
    )

    // =========================================================================
    // DATA CLASSES
    // =========================================================================

    /**
     * Complete sky view model for visualization.
     *
     * @property observerLocation Observer's position and computed sidereal parameters
     * @property observationTime The date/time of observation
     * @property localSiderealTime Local Sidereal Time in degrees (0-360)
     * @property obliquity Obliquity of the ecliptic in degrees at observation time
     * @property planetarySkyPositions Sky positions for all planets in the chart
     * @property nakshatraBoundaries Nakshatra boundary positions projected onto the sky
     * @property houseCuspPositions House cusp positions projected onto the sky
     * @property horizonData Horizon geometry data (cardinal points, ecliptic intersection)
     * @property visibilityReport Visibility assessment for each planet
     */
    data class SiderealSkyView(
        val observerLocation: ObserverLocation,
        val observationTime: LocalDateTime,
        val localSiderealTime: Double,
        val obliquity: Double,
        val planetarySkyPositions: List<PlanetSkyPosition>,
        val nakshatraBoundaries: List<NakshatraSkyPosition>,
        val houseCuspPositions: List<HouseCuspSkyPosition>,
        val horizonData: HorizonData,
        val visibilityReport: List<PlanetVisibility>
    )

    /**
     * Observer's geographic and sidereal parameters.
     */
    data class ObserverLocation(
        val latitude: Double,
        val longitude: Double,
        val timezone: String,
        val julianDay: Double,
        val gmst: Double,
        val lst: Double
    )

    /**
     * A planet's position in multiple coordinate systems, plus rise/set/culmination times.
     */
    data class PlanetSkyPosition(
        val planet: Planet,
        val eclipticLongitude: Double,
        val eclipticLatitude: Double,
        val rightAscension: Double,
        val declination: Double,
        val hourAngle: Double,
        val altitude: Double,
        val azimuth: Double,
        val isAboveHorizon: Boolean,
        val nakshatraName: String,
        val riseTime: LocalDateTime?,
        val setTime: LocalDateTime?,
        val culminationTime: LocalDateTime?,
        val transitAltitude: Double
    )

    /**
     * A nakshatra boundary projected onto the sky.
     */
    data class NakshatraSkyPosition(
        val nakshatra: Nakshatra,
        val startLongitude: Double,
        val endLongitude: Double,
        val startAltitude: Double,
        val startAzimuth: Double,
        val endAltitude: Double,
        val endAzimuth: Double,
        val isStartAboveHorizon: Boolean,
        val isEndAboveHorizon: Boolean,
        val midpointAltitude: Double,
        val midpointAzimuth: Double
    )

    /**
     * A house cusp projected onto the sky.
     */
    data class HouseCuspSkyPosition(
        val houseNumber: Int,
        val cuspLongitude: Double,
        val altitude: Double,
        val azimuth: Double,
        val isAboveHorizon: Boolean,
        val rightAscension: Double,
        val declination: Double
    )

    /**
     * Horizon geometry data.
     */
    data class HorizonData(
        val northPointAzimuth: Double,
        val eastPointAzimuth: Double,
        val southPointAzimuth: Double,
        val westPointAzimuth: Double,
        val eclipticNorthAltitude: Double,
        val eclipticSouthAltitude: Double,
        val ascendantAzimuth: Double,
        val descendantAzimuth: Double,
        val mhAltitude: Double,
        val icAltitude: Double
    )

    /**
     * Visibility assessment for a planet.
     */
    data class PlanetVisibility(
        val planet: Planet,
        val isAboveHorizon: Boolean,
        val isInSunGlare: Boolean,
        val isDaytimeSky: Boolean,
        val elongationFromSun: Double,
        val approximateMagnitude: Double,
        val visibilityStatus: VisibilityStatus,
        val description: String
    )

    enum class VisibilityStatus {
        EASILY_VISIBLE,
        VISIBLE_WITH_EFFORT,
        BARELY_VISIBLE,
        NOT_VISIBLE_BELOW_HORIZON,
        NOT_VISIBLE_SUN_GLARE,
        NOT_VISIBLE_DAYLIGHT,
        MATHEMATICAL_POINT
    }

    // =========================================================================
    // MAIN CALCULATION METHOD
    // =========================================================================

    /**
     * Calculate the complete sidereal sky view from a VedicChart.
     *
     * This method takes the chart's planetary positions (which are sidereal ecliptic
     * longitudes after ayanamsa correction) and the observer's geographic coordinates,
     * then computes the full sky view at the chart's observation time.
     *
     * For a natal chart, this shows the sky at the moment and place of birth.
     * For transit analysis, pass a different observation time.
     *
     * @param chart The VedicChart containing sidereal planetary positions
     * @param observationTime The date/time for the sky calculation (defaults to chart's birth time)
     * @return Complete [SiderealSkyView] with all coordinate transformations
     */
    fun calculateSkyView(
        chart: VedicChart,
        observationTime: LocalDateTime = chart.birthData.dateTime
    ): SiderealSkyView {
        val latitude = chart.birthData.latitude
        val longitude = chart.birthData.longitude
        val timezone = chart.birthData.timezone

        // 1. Convert observation time to Julian Day
        val jd = calculateJulianDay(observationTime, timezone)

        // 2. Calculate Julian centuries from J2000.0
        val T = (jd - J2000_EPOCH) / DAYS_PER_CENTURY

        // 3. Calculate obliquity of the ecliptic
        val obliquity = calculateObliquity(T)

        // 4. Calculate GMST and LST
        val gmst = calculateGMST(jd, T)
        val lst = normalizeDegrees(gmst + longitude)

        // 5. Create observer location
        val observer = ObserverLocation(
            latitude = latitude,
            longitude = longitude,
            timezone = timezone,
            julianDay = jd,
            gmst = gmst,
            lst = lst
        )

        // 6. Add ayanamsa back to get tropical ecliptic longitudes for coordinate conversion.
        // VedicChart stores sidereal longitudes; we need tropical for astronomical coordinate transforms.
        val ayanamsa = chart.ayanamsa

        // 7. Calculate sky positions for all planets
        val planetSkyPositions = chart.planetPositions.map { pos ->
            calculatePlanetSkyPosition(pos, ayanamsa, obliquity, lst, latitude, observationTime, timezone, jd)
        }

        // 8. Calculate nakshatra boundary positions
        val nakshatraBoundaries = calculateNakshatraBoundaries(ayanamsa, obliquity, lst, latitude)

        // 9. Calculate house cusp positions
        val houseCuspPositions = calculateHouseCuspPositions(chart, ayanamsa, obliquity, lst, latitude)

        // 10. Calculate horizon data
        val horizonData = calculateHorizonData(chart, ayanamsa, obliquity, lst, latitude)

        // 11. Calculate visibility report
        val sunPos = planetSkyPositions.find { it.planet == Planet.SUN }
        val visibilityReport = planetSkyPositions.map { pos ->
            assessVisibility(pos, sunPos, chart.planetPositions)
        }

        return SiderealSkyView(
            observerLocation = observer,
            observationTime = observationTime,
            localSiderealTime = lst,
            obliquity = obliquity,
            planetarySkyPositions = planetSkyPositions,
            nakshatraBoundaries = nakshatraBoundaries,
            houseCuspPositions = houseCuspPositions,
            horizonData = horizonData,
            visibilityReport = visibilityReport
        )
    }

    // =========================================================================
    // JULIAN DAY CALCULATION
    // =========================================================================

    /**
     * Calculate Julian Day Number from a LocalDateTime and timezone.
     *
     * Uses the standard Julian Day algorithm from Meeus (1998), Chapter 7.
     * The algorithm works for all dates in the Gregorian calendar.
     *
     * @param dateTime The local date/time
     * @param timezone The timezone string (e.g., "Asia/Kathmandu")
     * @return Julian Day Number (with fractional day)
     */
    private fun calculateJulianDay(dateTime: LocalDateTime, timezone: String): Double {
        // Convert local time to UTC
        val zoneId = resolveZoneId(timezone)
        val zonedDateTime = ZonedDateTime.of(dateTime, zoneId)
        val utcDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()

        val year = utcDateTime.year
        val month = utcDateTime.monthValue
        val day = utcDateTime.dayOfMonth
        val hour = utcDateTime.hour
        val minute = utcDateTime.minute
        val second = utcDateTime.second + utcDateTime.nano / 1_000_000_000.0

        val dayFraction = (hour + minute / 60.0 + second / 3600.0) / 24.0

        var y = year
        var m = month

        if (m <= 2) {
            y -= 1
            m += 12
        }

        val A = (y / 100)
        val B = 2 - A + (A / 4)

        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + dayFraction + B - 1524.5
    }

    // =========================================================================
    // SIDEREAL TIME CALCULATIONS
    // =========================================================================

    /**
     * Calculate Greenwich Mean Sidereal Time (GMST) in degrees.
     *
     * Uses the IAU 1982 expression (valid for dates near J2000.0):
     * ```
     * GMST = 280.46061837 + 360.98564736629 * (JD - 2451545.0)
     *        + 0.000387933 * T^2 - T^3 / 38710000.0
     * ```
     * where T = Julian centuries from J2000.0.
     *
     * @param jd Julian Day
     * @param T Julian centuries from J2000.0
     * @return GMST in degrees (0-360)
     */
    private fun calculateGMST(jd: Double, T: Double): Double {
        val gmst = 280.46061837 +
                360.98564736629 * (jd - J2000_EPOCH) +
                0.000387933 * T * T -
                T * T * T / 38710000.0
        return normalizeDegrees(gmst)
    }

    // =========================================================================
    // OBLIQUITY OF THE ECLIPTIC
    // =========================================================================

    /**
     * Calculate the mean obliquity of the ecliptic.
     *
     * Uses the IAU 2000 formula:
     * ```
     * epsilon = 23.439291111... - 0.0130042 * T - 1.64e-7 * T^2 + 5.04e-7 * T^3
     * ```
     *
     * A simplified nutation correction (delta-epsilon) is also applied:
     * ```
     * Omega = 125.04 - 1934.136 * T  (longitude of Moon's ascending node)
     * delta_epsilon = 0.00256 * cos(Omega)
     * ```
     *
     * @param T Julian centuries from J2000.0
     * @return True obliquity in degrees
     */
    private fun calculateObliquity(T: Double): Double {
        // Mean obliquity (Lieske 1979, IAU convention)
        val epsilonMean = 23.439291111 -
                0.0130042 * T -
                1.64e-7 * T * T +
                5.04e-7 * T * T * T

        // Nutation in obliquity (simplified)
        val omega = normalizeDegrees(125.04 - 1934.136 * T) * DEG_TO_RAD
        val deltaEpsilon = 0.00256 * cos(omega)

        return epsilonMean + deltaEpsilon
    }

    // =========================================================================
    // COORDINATE TRANSFORMATIONS
    // =========================================================================

    /**
     * Convert ecliptic coordinates (longitude, latitude) to equatorial (RA, Dec).
     *
     * Uses the standard spherical trigonometry transformations:
     * ```
     * sin(Dec) = sin(beta)*cos(epsilon) + cos(beta)*sin(epsilon)*sin(lambda)
     * tan(RA) = (sin(lambda)*cos(epsilon) - tan(beta)*sin(epsilon)) / cos(lambda)
     * ```
     * where lambda = ecliptic longitude, beta = ecliptic latitude, epsilon = obliquity.
     *
     * @param eclipticLongitude Ecliptic longitude in degrees
     * @param eclipticLatitude Ecliptic latitude in degrees
     * @param obliquity Obliquity of the ecliptic in degrees
     * @return Pair of (rightAscension, declination) both in degrees
     */
    private fun eclipticToEquatorial(
        eclipticLongitude: Double,
        eclipticLatitude: Double,
        obliquity: Double
    ): Pair<Double, Double> {
        val lambda = eclipticLongitude * DEG_TO_RAD
        val beta = eclipticLatitude * DEG_TO_RAD
        val epsilon = obliquity * DEG_TO_RAD

        // Declination
        val sinDec = sin(beta) * cos(epsilon) + cos(beta) * sin(epsilon) * sin(lambda)
        val declination = asin(sinDec.coerceIn(-1.0, 1.0)) * RAD_TO_DEG

        // Right Ascension
        val y = sin(lambda) * cos(epsilon) - tan(beta) * sin(epsilon)
        val x = cos(lambda)
        val rightAscension = normalizeDegrees(atan2(y, x) * RAD_TO_DEG)

        return rightAscension to declination
    }

    /**
     * Convert equatorial coordinates (RA, Dec) to horizontal (altitude, azimuth).
     *
     * Uses the standard equatorial-to-horizontal transformation:
     * ```
     * H = LST - RA                           (Hour Angle)
     * sin(alt) = sin(Dec)*sin(phi) + cos(Dec)*cos(phi)*cos(H)
     * tan(az)  = -sin(H) / (tan(Dec)*cos(phi) - sin(phi)*cos(H))
     * ```
     * where phi = observer latitude.
     *
     * Azimuth is measured from North (0) through East (90), South (180), West (270).
     *
     * @param rightAscension Right ascension in degrees
     * @param declination Declination in degrees
     * @param lst Local Sidereal Time in degrees
     * @param latitude Observer latitude in degrees
     * @return Triple of (altitude, azimuth, hourAngle) all in degrees
     */
    private fun equatorialToHorizontal(
        rightAscension: Double,
        declination: Double,
        lst: Double,
        latitude: Double
    ): Triple<Double, Double, Double> {
        val H = normalizeDegrees(lst - rightAscension) * DEG_TO_RAD  // Hour angle in radians
        val dec = declination * DEG_TO_RAD
        val phi = latitude * DEG_TO_RAD

        // Altitude
        val sinAlt = sin(dec) * sin(phi) + cos(dec) * cos(phi) * cos(H)
        val altitude = asin(sinAlt.coerceIn(-1.0, 1.0)) * RAD_TO_DEG

        // Azimuth (measured from North through East)
        val y = -sin(H)
        val x = tan(dec) * cos(phi) - sin(phi) * cos(H)
        val azimuth = normalizeDegrees(atan2(y, x) * RAD_TO_DEG)

        val hourAngleDeg = normalizeDegrees(lst - rightAscension)

        return Triple(altitude, azimuth, hourAngleDeg)
    }

    // =========================================================================
    // PLANET SKY POSITION CALCULATION
    // =========================================================================

    /**
     * Calculate the complete sky position for a single planet.
     *
     * Converts the planet's sidereal ecliptic position to tropical, then through
     * the equatorial and horizontal coordinate systems. Also computes rise/set/culmination
     * times and transit altitude.
     */
    private fun calculatePlanetSkyPosition(
        pos: PlanetPosition,
        ayanamsa: Double,
        obliquity: Double,
        lst: Double,
        latitude: Double,
        observationTime: LocalDateTime,
        timezone: String,
        jd: Double
    ): PlanetSkyPosition {
        // Convert sidereal to tropical for astronomical coordinate transformation
        val tropicalLongitude = normalizeDegrees(pos.longitude + ayanamsa)
        val eclipticLatitude = pos.latitude  // Ecliptic latitude from ephemeris

        // Ecliptic -> Equatorial
        val (ra, dec) = eclipticToEquatorial(tropicalLongitude, eclipticLatitude, obliquity)

        // Equatorial -> Horizontal
        val (altitude, azimuth, hourAngle) = equatorialToHorizontal(ra, dec, lst, latitude)

        val isAboveHorizon = altitude > 0.0

        // Rise/Set/Culmination times
        val (riseTime, setTime, culminationTime) = calculateRiseSetCulmination(
            ra, dec, latitude, lst, observationTime, timezone, jd
        )

        // Transit altitude (altitude when crossing the meridian)
        val transitAlt = calculateTransitAltitude(dec, latitude)

        // Nakshatra name from the sidereal longitude
        val (nakshatra, _) = Nakshatra.fromLongitude(pos.longitude)

        return PlanetSkyPosition(
            planet = pos.planet,
            eclipticLongitude = pos.longitude,  // Sidereal
            eclipticLatitude = eclipticLatitude,
            rightAscension = ra,
            declination = dec,
            hourAngle = hourAngle,
            altitude = altitude,
            azimuth = azimuth,
            isAboveHorizon = isAboveHorizon,
            nakshatraName = nakshatra.displayName,
            riseTime = riseTime,
            setTime = setTime,
            culminationTime = culminationTime,
            transitAltitude = transitAlt
        )
    }

    // =========================================================================
    // RISE / SET / CULMINATION
    // =========================================================================

    /**
     * Calculate rise, set, and culmination times for a celestial object.
     *
     * Uses the standard algorithm from Meeus (1998), Chapter 15.
     *
     * Rise/set occur when the object's altitude equals -(refraction + apparent_radius):
     * ```
     * cos(H0) = (sin(h0) - sin(phi)*sin(dec)) / (cos(phi)*cos(dec))
     * ```
     * where h0 = -0.5667 degrees for stars/planets (atmospheric refraction),
     * h0 = -0.8333 degrees for the Sun (refraction + semi-diameter).
     *
     * Culmination (upper transit) occurs when the hour angle is 0 (object on meridian).
     * Anti-culmination (lower transit) occurs when the hour angle is 180 degrees.
     *
     * @param ra Right ascension in degrees
     * @param dec Declination in degrees
     * @param latitude Observer latitude in degrees
     * @param lst Current Local Sidereal Time in degrees
     * @param observationTime Current observation time
     * @param timezone Timezone string
     * @param jd Julian Day of observation
     * @return Triple of (riseTime, setTime, culminationTime), any can be null if event doesn't occur
     */
    private fun calculateRiseSetCulmination(
        ra: Double,
        dec: Double,
        latitude: Double,
        lst: Double,
        observationTime: LocalDateTime,
        timezone: String,
        jd: Double
    ): Triple<LocalDateTime?, LocalDateTime?, LocalDateTime?> {
        val phi = latitude * DEG_TO_RAD
        val delta = dec * DEG_TO_RAD

        // Standard altitude for rise/set including atmospheric refraction
        val h0 = -ATMOSPHERIC_REFRACTION * DEG_TO_RAD

        // Calculate the hour angle at rise/set
        val cosH0Num = sin(h0) - sin(phi) * sin(delta)
        val cosH0Den = cos(phi) * cos(delta)

        // Check if the object ever rises or sets
        if (abs(cosH0Den) < 1e-10) {
            // Circumpolar or never rises
            val culminationTime = calculateCulminationTime(ra, lst, observationTime, timezone)
            return Triple(null, null, culminationTime)
        }

        val cosH0 = cosH0Num / cosH0Den

        if (cosH0 > 1.0) {
            // Object never rises (always below horizon)
            return Triple(null, null, null)
        }
        if (cosH0 < -1.0) {
            // Object never sets (circumpolar)
            val culminationTime = calculateCulminationTime(ra, lst, observationTime, timezone)
            return Triple(null, null, culminationTime)
        }

        val H0 = acos(cosH0) * RAD_TO_DEG  // Half arc (degrees)

        // Culmination: when hour angle = 0, i.e., LST = RA
        val culminationTime = calculateCulminationTime(ra, lst, observationTime, timezone)

        // Rise time: LST at rise = RA - H0
        val lstAtRise = normalizeDegrees(ra - H0)
        val riseTime = lstToLocalDateTime(lstAtRise, lst, observationTime, timezone)

        // Set time: LST at set = RA + H0
        val lstAtSet = normalizeDegrees(ra + H0)
        val setTime = lstToLocalDateTime(lstAtSet, lst, observationTime, timezone)

        return Triple(riseTime, setTime, culminationTime)
    }

    /**
     * Calculate the time of upper meridian transit (culmination).
     */
    private fun calculateCulminationTime(
        ra: Double,
        lst: Double,
        observationTime: LocalDateTime,
        timezone: String
    ): LocalDateTime? {
        return lstToLocalDateTime(ra, lst, observationTime, timezone)
    }

    /**
     * Convert a target LST to a local date/time.
     *
     * Computes how many sidereal hours until the target LST, then converts
     * that to mean solar time and adds to the observation time.
     *
     * The sidereal day is 23h 56m 4.091s of mean solar time, so:
     * ```
     * solar_hours = sidereal_hours * (23.9344694 / 24.0)
     * ```
     *
     * @param targetLst The target LST in degrees (0-360)
     * @param currentLst The current LST in degrees
     * @param observationTime The current observation time
     * @param timezone Timezone string
     * @return Local date/time of the target LST, or null if calculation fails
     */
    private fun lstToLocalDateTime(
        targetLst: Double,
        currentLst: Double,
        observationTime: LocalDateTime,
        timezone: String
    ): LocalDateTime? {
        return try {
            // Difference in LST (degrees)
            var lstDiff = normalizeDegrees(targetLst - currentLst)
            if (lstDiff > 180.0) lstDiff -= 360.0
            if (lstDiff < -180.0) lstDiff += 360.0

            // Convert LST difference (degrees) to sidereal hours
            val siderealHours = lstDiff / 15.0

            // Convert sidereal time to mean solar time
            // Ratio: 1 sidereal day = 23h56m4.091s = 23.9344694... solar hours
            val solarHours = siderealHours * (24.0 / 24.06570982441908)

            // Add to observation time
            val totalSeconds = (solarHours * 3600.0).toLong()
            observationTime.plusSeconds(totalSeconds)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Calculate the transit (culmination) altitude of an object.
     *
     * At upper meridian transit: altitude = 90 - |latitude - declination|
     * (simplified for standard cases)
     *
     * More precisely:
     * ```
     * alt_transit = 90 - |phi - dec|    (if object transits south of zenith in NH)
     * alt_transit = 90 - (phi - dec)     (general)
     * ```
     *
     * @param declination Declination in degrees
     * @param latitude Observer latitude in degrees
     * @return Transit altitude in degrees
     */
    private fun calculateTransitAltitude(declination: Double, latitude: Double): Double {
        // At transit, hour angle = 0, so sin(alt) = sin(dec)*sin(phi) + cos(dec)*cos(phi)
        val phi = latitude * DEG_TO_RAD
        val dec = declination * DEG_TO_RAD
        val sinAlt = sin(dec) * sin(phi) + cos(dec) * cos(phi)
        return asin(sinAlt.coerceIn(-1.0, 1.0)) * RAD_TO_DEG
    }

    // =========================================================================
    // NAKSHATRA BOUNDARY CALCULATION
    // =========================================================================

    /**
     * Calculate nakshatra boundaries projected onto the sky.
     *
     * Each of the 27 nakshatras spans 13 degrees 20 minutes of sidereal ecliptic longitude.
     * The start and end points of each nakshatra are converted to tropical coordinates,
     * then to equatorial, then to horizontal, providing their altitude/azimuth positions
     * for overlay visualization.
     */
    private fun calculateNakshatraBoundaries(
        ayanamsa: Double,
        obliquity: Double,
        lst: Double,
        latitude: Double
    ): List<NakshatraSkyPosition> {
        return Nakshatra.values().map { nakshatra ->
            val siderealStart = nakshatra.startDegree
            val siderealEnd = nakshatra.endDegree
            val siderealMid = (siderealStart + siderealEnd) / 2.0

            // Convert to tropical for coordinate transformation
            val tropStart = normalizeDegrees(siderealStart + ayanamsa)
            val tropEnd = normalizeDegrees(siderealEnd + ayanamsa)
            val tropMid = normalizeDegrees(siderealMid + ayanamsa)

            // Assume zero ecliptic latitude for nakshatra boundaries (they lie on the ecliptic)
            val (raStart, decStart) = eclipticToEquatorial(tropStart, 0.0, obliquity)
            val (altStart, azStart, _) = equatorialToHorizontal(raStart, decStart, lst, latitude)

            val (raEnd, decEnd) = eclipticToEquatorial(tropEnd, 0.0, obliquity)
            val (altEnd, azEnd, _) = equatorialToHorizontal(raEnd, decEnd, lst, latitude)

            val (raMid, decMid) = eclipticToEquatorial(tropMid, 0.0, obliquity)
            val (altMid, azMid, _) = equatorialToHorizontal(raMid, decMid, lst, latitude)

            NakshatraSkyPosition(
                nakshatra = nakshatra,
                startLongitude = siderealStart,
                endLongitude = siderealEnd,
                startAltitude = altStart,
                startAzimuth = azStart,
                endAltitude = altEnd,
                endAzimuth = azEnd,
                isStartAboveHorizon = altStart > 0.0,
                isEndAboveHorizon = altEnd > 0.0,
                midpointAltitude = altMid,
                midpointAzimuth = azMid
            )
        }
    }

    // =========================================================================
    // HOUSE CUSP POSITIONS
    // =========================================================================

    /**
     * Calculate house cusp positions projected onto the sky.
     *
     * Each of the 12 house cusps from the chart is an ecliptic longitude.
     * These are converted through the same coordinate pipeline to provide
     * altitude/azimuth positions for overlay visualization.
     */
    private fun calculateHouseCuspPositions(
        chart: VedicChart,
        ayanamsa: Double,
        obliquity: Double,
        lst: Double,
        latitude: Double
    ): List<HouseCuspSkyPosition> {
        return chart.houseCusps.mapIndexed { index, cuspLongitude ->
            val houseNumber = index + 1
            // Cusp longitudes in VedicChart are sidereal
            val tropicalLong = normalizeDegrees(cuspLongitude + ayanamsa)

            val (ra, dec) = eclipticToEquatorial(tropicalLong, 0.0, obliquity)
            val (alt, az, _) = equatorialToHorizontal(ra, dec, lst, latitude)

            HouseCuspSkyPosition(
                houseNumber = houseNumber,
                cuspLongitude = cuspLongitude,
                altitude = alt,
                azimuth = az,
                isAboveHorizon = alt > 0.0,
                rightAscension = ra,
                declination = dec
            )
        }
    }

    // =========================================================================
    // HORIZON DATA
    // =========================================================================

    /**
     * Calculate horizon geometry data.
     *
     * Determines the positions of the ascendant, descendant, MC, and IC
     * in the horizontal coordinate system, plus the ecliptic's intersection
     * with the horizon.
     */
    private fun calculateHorizonData(
        chart: VedicChart,
        ayanamsa: Double,
        obliquity: Double,
        lst: Double,
        latitude: Double
    ): HorizonData {
        // Ascendant (rising point of ecliptic)
        val ascTropical = normalizeDegrees(chart.ascendant + ayanamsa)
        val (ascRa, ascDec) = eclipticToEquatorial(ascTropical, 0.0, obliquity)
        val (_, ascAz, _) = equatorialToHorizontal(ascRa, ascDec, lst, latitude)

        // Descendant (setting point of ecliptic = ascendant + 180)
        val descTropical = normalizeDegrees(ascTropical + 180.0)
        val (descRa, descDec) = eclipticToEquatorial(descTropical, 0.0, obliquity)
        val (_, descAz, _) = equatorialToHorizontal(descRa, descDec, lst, latitude)

        // MC (Midheaven) - highest point of ecliptic
        val mcTropical = normalizeDegrees(chart.midheaven + ayanamsa)
        val (mcRa, mcDec) = eclipticToEquatorial(mcTropical, 0.0, obliquity)
        val (mcAlt, _, _) = equatorialToHorizontal(mcRa, mcDec, lst, latitude)

        // IC (Imum Coeli) = MC + 180
        val icTropical = normalizeDegrees(mcTropical + 180.0)
        val (icRa, icDec) = eclipticToEquatorial(icTropical, 0.0, obliquity)
        val (icAlt, _, _) = equatorialToHorizontal(icRa, icDec, lst, latitude)

        // Ecliptic pole altitude (north ecliptic pole: ecliptic latitude = 90)
        // The north ecliptic pole has equatorial coordinates:
        // Dec_NEP = 90 - obliquity, RA_NEP = 270 (18h)
        val nepDec = 90.0 - obliquity
        val nepRa = 270.0
        val (nepAlt, _, _) = equatorialToHorizontal(nepRa, nepDec, lst, latitude)

        // South ecliptic pole
        val sepDec = -(90.0 - obliquity)
        val sepRa = 90.0
        val (sepAlt, _, _) = equatorialToHorizontal(sepRa, sepDec, lst, latitude)

        return HorizonData(
            northPointAzimuth = 0.0,
            eastPointAzimuth = 90.0,
            southPointAzimuth = 180.0,
            westPointAzimuth = 270.0,
            eclipticNorthAltitude = nepAlt,
            eclipticSouthAltitude = sepAlt,
            ascendantAzimuth = ascAz,
            descendantAzimuth = descAz,
            mhAltitude = mcAlt,
            icAltitude = icAlt
        )
    }

    // =========================================================================
    // VISIBILITY ASSESSMENT
    // =========================================================================

    /**
     * Assess the visibility of a planet in the sky.
     *
     * Considers:
     * - Whether the planet is above the horizon
     * - Whether it's within the Sun's glare (elongation from Sun)
     * - Whether the sky is daylit (Sun above horizon)
     * - Approximate visual magnitude
     * - Whether the planet is a mathematical point (Rahu/Ketu)
     */
    private fun assessVisibility(
        planetSky: PlanetSkyPosition,
        sunSky: PlanetSkyPosition?,
        natalPositions: List<PlanetPosition>
    ): PlanetVisibility {
        val planet = planetSky.planet

        // Rahu and Ketu are mathematical points - not visible
        if (planet == Planet.RAHU || planet == Planet.KETU || planet == Planet.TRUE_NODE) {
            return PlanetVisibility(
                planet = planet,
                isAboveHorizon = planetSky.isAboveHorizon,
                isInSunGlare = false,
                isDaytimeSky = sunSky?.isAboveHorizon == true,
                elongationFromSun = 0.0,
                approximateMagnitude = 99.0,
                visibilityStatus = VisibilityStatus.MATHEMATICAL_POINT,
                description = "${planet.displayName} is a mathematical point (lunar node) with no physical body to observe."
            )
        }

        val isDaytime = sunSky?.isAboveHorizon == true

        // Calculate elongation from Sun
        val elongation = if (sunSky != null && planet != Planet.SUN) {
            val natalPos = natalPositions.find { it.planet == planet }
            val sunNatalPos = natalPositions.find { it.planet == Planet.SUN }
            if (natalPos != null && sunNatalPos != null) {
                VedicAstrologyUtils.angularDistance(natalPos.longitude, sunNatalPos.longitude)
            } else {
                180.0
            }
        } else {
            0.0
        }

        val glareLimit = SOLAR_GLARE_LIMITS[planet] ?: 2.0
        val isInGlare = planet != Planet.SUN && elongation < glareLimit

        val magnitude = APPROXIMATE_MAGNITUDES[planet] ?: 10.0

        // Determine visibility status
        val status: VisibilityStatus
        val description: String

        when {
            planet == Planet.SUN -> {
                status = if (planetSky.isAboveHorizon) VisibilityStatus.EASILY_VISIBLE
                else VisibilityStatus.NOT_VISIBLE_BELOW_HORIZON
                description = if (planetSky.isAboveHorizon) {
                    "The Sun is above the horizon at altitude ${String.format("%.1f", planetSky.altitude)} degrees, " +
                            "azimuth ${String.format("%.1f", planetSky.azimuth)} degrees."
                } else {
                    "The Sun is below the horizon (altitude ${String.format("%.1f", planetSky.altitude)} degrees). Night sky."
                }
            }
            !planetSky.isAboveHorizon -> {
                status = VisibilityStatus.NOT_VISIBLE_BELOW_HORIZON
                description = "${planet.displayName} is below the horizon at altitude ${String.format("%.1f", planetSky.altitude)} degrees. " +
                        "It will rise at ${planetSky.riseTime?.let { formatTime(it) } ?: "unknown time"}."
            }
            isInGlare -> {
                status = VisibilityStatus.NOT_VISIBLE_SUN_GLARE
                description = "${planet.displayName} is within ${String.format("%.1f", elongation)} degrees of the Sun " +
                        "and lost in solar glare. It is technically above the horizon but cannot be observed."
            }
            isDaytime && planet != Planet.MOON && planet != Planet.VENUS -> {
                status = VisibilityStatus.NOT_VISIBLE_DAYLIGHT
                description = "${planet.displayName} is above the horizon but the Sun is also up. " +
                        "Daylight makes it invisible to the naked eye."
            }
            isDaytime && planet == Planet.VENUS && elongation > 40.0 -> {
                status = VisibilityStatus.VISIBLE_WITH_EFFORT
                description = "Venus is visible in daylight as the 'morning/evening star' at ${String.format("%.1f", elongation)} degrees from the Sun. " +
                        "Look near the horizon in the direction of azimuth ${String.format("%.1f", planetSky.azimuth)} degrees."
            }
            isDaytime && planet == Planet.MOON -> {
                status = VisibilityStatus.VISIBLE_WITH_EFFORT
                description = "The Moon is visible in the daytime sky at altitude ${String.format("%.1f", planetSky.altitude)} degrees, " +
                        "azimuth ${String.format("%.1f", planetSky.azimuth)} degrees."
            }
            magnitude > 6.0 -> {
                status = VisibilityStatus.BARELY_VISIBLE
                description = "${planet.displayName} is above the horizon at altitude ${String.format("%.1f", planetSky.altitude)} degrees " +
                        "but requires a telescope (magnitude ~${String.format("%.1f", magnitude)})."
            }
            magnitude > 4.0 -> {
                status = VisibilityStatus.VISIBLE_WITH_EFFORT
                description = "${planet.displayName} is visible with some effort at altitude ${String.format("%.1f", planetSky.altitude)} degrees, " +
                        "azimuth ${String.format("%.1f", planetSky.azimuth)} degrees. Magnitude ~${String.format("%.1f", magnitude)}."
            }
            else -> {
                status = VisibilityStatus.EASILY_VISIBLE
                description = "${planet.displayName} is easily visible in the night sky at altitude ${String.format("%.1f", planetSky.altitude)} degrees, " +
                        "azimuth ${String.format("%.1f", planetSky.azimuth)} degrees (${getCardinalDirection(planetSky.azimuth)}). " +
                        "Look in the nakshatra ${planetSky.nakshatraName}. Magnitude ~${String.format("%.1f", magnitude)}."
            }
        }

        return PlanetVisibility(
            planet = planet,
            isAboveHorizon = planetSky.isAboveHorizon,
            isInSunGlare = isInGlare,
            isDaytimeSky = isDaytime,
            elongationFromSun = elongation,
            approximateMagnitude = magnitude,
            visibilityStatus = status,
            description = description
        )
    }

    /**
     * Get a cardinal direction label from an azimuth angle.
     */
    private fun getCardinalDirection(azimuth: Double): String {
        val normalized = normalizeDegrees(azimuth)
        return when {
            normalized < 22.5 || normalized >= 337.5 -> "North"
            normalized < 67.5 -> "North-East"
            normalized < 112.5 -> "East"
            normalized < 157.5 -> "South-East"
            normalized < 202.5 -> "South"
            normalized < 247.5 -> "South-West"
            normalized < 292.5 -> "West"
            else -> "North-West"
        }
    }

    /**
     * Format a LocalDateTime as a time string.
     */
    private fun formatTime(dateTime: LocalDateTime): String {
        return String.format("%02d:%02d", dateTime.hour, dateTime.minute)
    }

    // =========================================================================
    // UTILITY METHODS
    // =========================================================================

    /**
     * Normalize an angle to the range [0, 360).
     */
    private fun normalizeDegrees(degrees: Double): Double {
        var result = degrees % 360.0
        if (result < 0.0) result += 360.0
        return result
    }

    /**
     * Resolve a timezone string to a ZoneId, falling back to UTC if invalid.
     */
    private fun resolveZoneId(timezone: String?): ZoneId {
        return try {
            if (timezone.isNullOrBlank()) ZoneOffset.UTC
            else com.astro.vajra.util.TimezoneSanitizer.resolveZoneIdOrNull(timezone) ?: ZoneOffset.UTC
        } catch (e: Exception) {
            ZoneOffset.UTC
        }
    }

    // =========================================================================
    // CONVENIENCE METHODS
    // =========================================================================

    /**
     * Quick check: is a planet currently above the horizon?
     *
     * @param chart The VedicChart
     * @param planet The planet to check
     * @param observationTime Time of observation (defaults to birth time)
     * @return True if the planet is above the local horizon
     */
    fun isPlanetAboveHorizon(
        chart: VedicChart,
        planet: Planet,
        observationTime: LocalDateTime = chart.birthData.dateTime
    ): Boolean {
        val skyView = calculateSkyView(chart, observationTime)
        return skyView.planetarySkyPositions.find { it.planet == planet }?.isAboveHorizon == true
    }

    /**
     * Get the altitude and azimuth of a specific planet.
     *
     * @param chart The VedicChart
     * @param planet The planet to query
     * @param observationTime Time of observation
     * @return Pair of (altitude, azimuth) in degrees, or null if planet not found
     */
    fun getPlanetAltAz(
        chart: VedicChart,
        planet: Planet,
        observationTime: LocalDateTime = chart.birthData.dateTime
    ): Pair<Double, Double>? {
        val skyView = calculateSkyView(chart, observationTime)
        val pos = skyView.planetarySkyPositions.find { it.planet == planet } ?: return null
        return pos.altitude to pos.azimuth
    }

    /**
     * Get the Local Sidereal Time for a given chart and observation time.
     *
     * @param chart The VedicChart (provides location)
     * @param observationTime The observation time
     * @return LST in degrees (0-360)
     */
    fun getLocalSiderealTime(
        chart: VedicChart,
        observationTime: LocalDateTime = chart.birthData.dateTime
    ): Double {
        val jd = calculateJulianDay(observationTime, chart.birthData.timezone)
        val T = (jd - J2000_EPOCH) / DAYS_PER_CENTURY
        val gmst = calculateGMST(jd, T)
        return normalizeDegrees(gmst + chart.birthData.longitude)
    }

    /**
     * Get all planets that are above the horizon at the given time.
     *
     * @param chart The VedicChart
     * @param observationTime Time of observation
     * @return List of planets above the horizon
     */
    fun getPlanetsAboveHorizon(
        chart: VedicChart,
        observationTime: LocalDateTime = chart.birthData.dateTime
    ): List<Planet> {
        val skyView = calculateSkyView(chart, observationTime)
        return skyView.planetarySkyPositions
            .filter { it.isAboveHorizon }
            .map { it.planet }
    }

    /**
     * Get all planets that are currently visible (above horizon, not in sun glare, night sky).
     *
     * @param chart The VedicChart
     * @param observationTime Time of observation
     * @return List of visible planet positions with their sky coordinates
     */
    fun getVisiblePlanets(
        chart: VedicChart,
        observationTime: LocalDateTime = chart.birthData.dateTime
    ): List<PlanetSkyPosition> {
        val skyView = calculateSkyView(chart, observationTime)
        val visiblePlanets = skyView.visibilityReport
            .filter {
                it.visibilityStatus == VisibilityStatus.EASILY_VISIBLE ||
                        it.visibilityStatus == VisibilityStatus.VISIBLE_WITH_EFFORT
            }
            .map { it.planet }
            .toSet()
        return skyView.planetarySkyPositions.filter { it.planet in visiblePlanets }
    }

    /**
     * Calculate which nakshatras are currently visible above the horizon.
     *
     * @param chart The VedicChart
     * @param observationTime Time of observation
     * @return List of nakshatra sky positions where at least part is above the horizon
     */
    fun getVisibleNakshatras(
        chart: VedicChart,
        observationTime: LocalDateTime = chart.birthData.dateTime
    ): List<NakshatraSkyPosition> {
        val skyView = calculateSkyView(chart, observationTime)
        return skyView.nakshatraBoundaries.filter {
            it.isStartAboveHorizon || it.isEndAboveHorizon || it.midpointAltitude > 0.0
        }
    }
}
