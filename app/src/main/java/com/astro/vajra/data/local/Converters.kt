package com.astro.vajra.data.local

import androidx.room.TypeConverter
import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.ZodiacSign
import org.json.JSONArray
import org.json.JSONObject

/**
 * Type converters for Room database
 */
class Converters {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        if (value == null) return null
        return try {
            val jsonArray = JSONArray(value)
            List(jsonArray.length()) { jsonArray.getString(it) }
        } catch (e: Exception) {
            value.split(",").map { it.trim() }
        }
    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        if (list == null) return null
        return JSONArray(list).toString()
    }

    @TypeConverter
    fun fromDoubleListJson(value: String?): List<Double>? {
        if (value == null) return null
        return try {
            val jsonArray = JSONArray(value)
            List(jsonArray.length()) { jsonArray.getDouble(it) }
        } catch (e: Exception) {
            value.split(",").mapNotNull { it.toDoubleOrNull() }
        }
    }

    @TypeConverter
    fun toDoubleListJson(list: List<Double>?): String? {
        if (list == null) return null
        return JSONArray(list).toString()
    }

    @TypeConverter
    fun fromPlanetPositionsJson(value: String?): List<PlanetPosition>? {
        if (value == null) return null
        return try {
            val array = JSONArray(value)
            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                PlanetPosition(
                    planet = Planet.valueOf(obj.getString("planet")),
                    longitude = obj.getDouble("longitude"),
                    latitude = obj.getDouble("latitude"),
                    distance = obj.getDouble("distance"),
                    speed = obj.getDouble("speed"),
                    sign = ZodiacSign.valueOf(obj.getString("sign")),
                    degree = obj.getDouble("degree"),
                    minutes = obj.getDouble("minutes"),
                    seconds = obj.getDouble("seconds"),
                    isRetrograde = obj.getBoolean("isRetrograde"),
                    nakshatra = Nakshatra.valueOf(obj.getString("nakshatra")),
                    nakshatraPada = obj.getInt("nakshatraPada"),
                    house = obj.getInt("house")
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toPlanetPositionsJson(list: List<PlanetPosition>?): String? {
        if (list == null) return null
        return JSONArray().apply {
            list.forEach { position ->
                put(JSONObject().apply {
                    put("planet", position.planet.name)
                    put("longitude", position.longitude)
                    put("latitude", position.latitude)
                    put("distance", position.distance)
                    put("speed", position.speed)
                    put("sign", position.sign.name)
                    put("degree", position.degree)
                    put("minutes", position.minutes)
                    put("seconds", position.seconds)
                    put("isRetrograde", position.isRetrograde)
                    put("nakshatra", position.nakshatra.name)
                    put("nakshatraPada", position.nakshatraPada)
                    put("house", position.house)
                })
            }
        }.toString()
    }
}
