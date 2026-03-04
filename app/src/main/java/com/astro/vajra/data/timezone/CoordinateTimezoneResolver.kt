package com.astro.vajra.data.timezone

import android.util.Log
import com.astro.vajra.util.TimezoneSanitizer
import us.dustinj.timezonemap.TimeZoneMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.floor

/**
 * Offline timezone resolver based on timezone boundary polygons.
 *
 * Uses regional caching so repeated lookups are fast and do not require network access.
 */
@Singleton
class CoordinateTimezoneResolver @Inject constructor() {

    private data class RegionKey(
        val southLat: Double,
        val westLon: Double,
        val northLat: Double,
        val eastLon: Double
    ) {
        companion object {
            fun fromCoordinate(latitude: Double, longitude: Double, tileSize: Double): RegionKey {
                val latBucket = floor((latitude + 90.0) / tileSize).toInt()
                val lonBucket = floor((longitude + 180.0) / tileSize).toInt()

                val south = (-90.0 + latBucket * tileSize).coerceIn(-90.0, 90.0 - REGION_EPSILON)
                val west = (-180.0 + lonBucket * tileSize).coerceIn(-180.0, 180.0 - REGION_EPSILON)
                val north = (south + tileSize).coerceIn(-90.0 + REGION_EPSILON, 90.0)
                val east = (west + tileSize).coerceIn(-180.0 + REGION_EPSILON, 180.0)

                return RegionKey(
                    southLat = south,
                    westLon = west,
                    northLat = north,
                    eastLon = east
                )
            }
        }
    }

    private val cacheLock = Any()
    private val regionCache = object : LinkedHashMap<RegionKey, TimeZoneMap>(
        MAX_REGION_CACHE_SIZE,
        0.75f,
        true
    ) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<RegionKey, TimeZoneMap>): Boolean {
            return size > MAX_REGION_CACHE_SIZE
        }
    }

    fun resolveTimezoneIdOrNull(latitude: Double, longitude: Double): String? {
        if (!isValidLatitude(latitude) || !isValidLongitude(longitude)) {
            return null
        }

        val safeLat = latitude.coerceIn(-90.0 + REGION_EPSILON, 90.0 - REGION_EPSILON)
        val safeLon = longitude.coerceIn(-180.0 + REGION_EPSILON, 180.0 - REGION_EPSILON)
        val primaryKey = RegionKey.fromCoordinate(safeLat, safeLon, REGION_TILE_DEGREES)

        val primaryResult = findTimezoneInRegion(primaryKey, safeLat, safeLon)
        if (primaryResult != null) return primaryResult

        // Boundary fallback: retry with a larger regional tile around the same coordinate.
        val fallbackKey = RegionKey.fromCoordinate(safeLat, safeLon, FALLBACK_REGION_TILE_DEGREES)

        return if (fallbackKey == primaryKey) {
            null
        } else {
            findTimezoneInRegion(fallbackKey, safeLat, safeLon)
        }
    }

    private fun findTimezoneInRegion(
        regionKey: RegionKey,
        latitude: Double,
        longitude: Double
    ): String? {
        val map = getOrCreateRegionMap(regionKey) ?: return null
        val timezone = runCatching {
            map.getOverlappingTimeZone(latitude, longitude)
        }.getOrElse { error ->
            Log.w(TAG, "Timezone lookup failed for coordinate ($latitude, $longitude)", error)
            null
        } ?: return null

        return TimezoneSanitizer.resolveZoneIdOrNull(timezone.zoneId)?.id
    }

    private fun getOrCreateRegionMap(regionKey: RegionKey): TimeZoneMap? {
        synchronized(cacheLock) {
            regionCache[regionKey]?.let { return it }
        }

        val map = runCatching {
            TimeZoneMap.forRegion(
                regionKey.southLat,
                regionKey.westLon,
                regionKey.northLat,
                regionKey.eastLon
            )
        }.getOrElse { error ->
            Log.e(TAG, "Failed to initialize timezone map for region: $regionKey", error)
            return null
        }

        synchronized(cacheLock) {
            regionCache[regionKey] = map
        }
        return map
    }

    private fun isValidLatitude(value: Double): Boolean {
        return value in -90.0..90.0
    }

    private fun isValidLongitude(value: Double): Boolean {
        return value in -180.0..180.0
    }

    companion object {
        private const val TAG = "CoordinateTimezoneResolver"
        private const val REGION_TILE_DEGREES = 15.0
        private const val FALLBACK_REGION_TILE_DEGREES = 45.0
        private const val MAX_REGION_CACHE_SIZE = 8
        private const val REGION_EPSILON = 1e-6
    }
}
