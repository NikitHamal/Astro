package com.astro.storm.data.api

import android.util.Log
import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

/**
 * Production-grade Geocoding Service using OpenStreetMap Nominatim API.
 *
 * Features:
 * - Strict Rate Limiting (1 req/sec) to comply with Nominatim TOS.
 * - In-Memory Caching (LruCache) for performance.
 * - Detailed parsing (City, State, Country, Timezone).
 * - Automatic thread safety.
 *
 * API Ref: https://nominatim.org/release-docs/latest/api/Search/
 */
object GeocodingService {
    private const val TAG = "GeocodingService"
    private const val BASE_URL = "https://nominatim.openstreetmap.org"
    // IMPORTANT: Replace with your specific contact email to comply with TOS if high volume
    private const val USER_AGENT = "AstroStorm/1.0 (android; vedic_astrology_app)" 
    private const val CONNECT_TIMEOUT = 10_000 // 10s
    private const val READ_TIMEOUT = 15_000    // 15s

    // Rate Limiting variables
    private val apiMutex = Mutex()
    private var lastRequestTime = 0L
    private const val MIN_REQUEST_INTERVAL = 1100L // 1.1 seconds buffer

    // Cache (Stores last 50 queries to reduce network calls)
    private val searchCache = LruCache<String, List<GeocodingResult>>(50)

    /**
     * Data model optimized for Astrology needs.
     * Includes Timezone and formatted location parts.
     */
    data class GeocodingResult(
        val displayName: String,      // Full raw name
        val formattedName: String,    // Clean UI name (City, State, Country)
        val latitude: Double,
        val longitude: Double,
        val timezone: String?,        // Critical for Astrology charts
        val city: String,
        val country: String,
        val countryCode: String
    )

    /**
     * Search for locations.
     * Handles caching, threading, and rate limiting automatically.
     */
    suspend fun searchLocation(
        query: String,
        limit: Int = 5
    ): Result<List<GeocodingResult>> = withContext(Dispatchers.IO) {
        if (query.trim().length < 2) return@withContext Result.success(emptyList())

        val sanitizedQuery = query.trim()
        
        // 1. Check Cache
        searchCache.get(sanitizedQuery)?.let {
            Log.d(TAG, "Cache hit for: $sanitizedQuery")
            return@withContext Result.success(it)
        }

        // 2. Perform Network Request with Rate Limiting
        try {
            val results = executeSearchWithRateLimit(sanitizedQuery, limit)
            
            // 3. Update Cache
            if (results.isNotEmpty()) {
                searchCache.put(sanitizedQuery, results)
            }
            
            Result.success(results)
        } catch (e: Exception) {
            Log.e(TAG, "Search failed for: $sanitizedQuery", e)
            Result.failure(e)
        }
    }

    /**
     * Reverse geocode coordinates to get location details.
     */
    suspend fun reverseGeocode(
        latitude: Double,
        longitude: Double
    ): Result<GeocodingResult?> = withContext(Dispatchers.IO) {
        // Create a cache key based on coords rounded to ~100m precision
        val cacheKey = "rev_${String.format(Locale.US, "%.3f_%.3f", latitude, longitude)}"
        
        // Check simple cache for reverse lookup (re-using the result list structure)
        searchCache.get(cacheKey)?.firstOrNull()?.let {
            return@withContext Result.success(it)
        }

        apiMutex.withLock {
            enforceRateLimit()
            try {
                // Fetch address details AND extra tags (for timezone)
                val urlString = "$BASE_URL/reverse?format=json&lat=$latitude&lon=$longitude&zoom=10&addressdetails=1&extratags=1"
                val responseJson = fetchJson(urlString)
                
                // Nominatim reverse returns a single object, not an array
                val obj = JSONObject(responseJson)
                val result = parseSingleResult(obj)

                if (result != null) {
                    searchCache.put(cacheKey, listOf(result))
                }
                
                Result.success(result)
            } catch (e: Exception) {
                Log.e(TAG, "Reverse geocoding failed", e)
                Result.failure(e)
            }
        }
    }

    private suspend fun executeSearchWithRateLimit(query: String, limit: Int): List<GeocodingResult> {
        // Mutex ensures we don't fire parallel requests violating TOS
        apiMutex.withLock {
            enforceRateLimit()

            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            // addressdetails=1: Get structured address
            // extratags=1: Get Timezone info
            val urlString = "$BASE_URL/search?format=json&q=$encodedQuery&limit=$limit&addressdetails=1&extratags=1"
            
            val responseJson = fetchJson(urlString)
            return parseSearchResults(responseJson)
        }
    }

    private suspend fun enforceRateLimit() {
        val currentTime = System.currentTimeMillis()
        val timeSinceLast = currentTime - lastRequestTime
        if (timeSinceLast < MIN_REQUEST_INTERVAL) {
            val waitTime = MIN_REQUEST_INTERVAL - timeSinceLast
            delay(waitTime)
        }
        lastRequestTime = System.currentTimeMillis()
    }

    private fun fetchJson(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        connection.apply {
            requestMethod = "GET"
            // Crucial: Nominatim requires a valid User-Agent identifying the app
            setRequestProperty("User-Agent", USER_AGENT)
            setRequestProperty("Accept", "application/json")
            connectTimeout = CONNECT_TIMEOUT
            readTimeout = READ_TIMEOUT
            doInput = true
        }

        try {
            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                // Handle specific Nominatim errors
                if (responseCode == 403 || responseCode == 429) {
                    throw Exception("API Rate Limit Exceeded or Ban (HTTP $responseCode)")
                }
                throw Exception("HTTP Error: $responseCode")
            }

            return connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    private fun parseSearchResults(json: String): List<GeocodingResult> {
        val results = mutableListOf<GeocodingResult>()
        try {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                parseSingleResult(obj)?.let { results.add(it) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "JSON Parsing error", e)
        }
        // Return most relevant results first
        return results
    }

    private fun parseSingleResult(obj: JSONObject): GeocodingResult? {
        return try {
            val addressObj = obj.optJSONObject("address")
            val extraTags = obj.optJSONObject("extratags")
            
            // Extract hierarchy for Vedic Astrology (City/Town is priority)
            val city = addressObj?.optString("city")
                .takeIf { !it.isNullOrBlank() }
                ?: addressObj?.optString("town")
                .takeIf { !it.isNullOrBlank() }
                ?: addressObj?.optString("village")
                .takeIf { !it.isNullOrBlank() }
                ?: addressObj?.optString("county")
                .takeIf { !it.isNullOrBlank() }
                ?: "Unknown Location"

            val state = addressObj?.optString("state") ?: ""
            val country = addressObj?.optString("country") ?: ""
            val countryCode = addressObj?.optString("country_code", "")?.uppercase() ?: ""

            // Construct a clean "City, State, Country" string for UI
            val parts = listOf(city, state, country).filter { it.isNotBlank() }
            val formattedName = parts.joinToString(", ")

            // Extract Timezone (Critical for Astrology)
            // Nominatim usually returns "timezone" inside "extratags"
            val timezone = extraTags?.optString("timezone") 
                ?: extraTags?.optString("time_zone")

            GeocodingResult(
                displayName = obj.optString("display_name"),
                formattedName = formattedName,
                latitude = obj.getDouble("lat"),
                longitude = obj.getDouble("lon"),
                timezone = timezone,
                city = city,
                country = country,
                countryCode = countryCode
            )
        } catch (e: Exception) {
            Log.w(TAG, "Skipping invalid result item", e)
            null
        }
    }
}