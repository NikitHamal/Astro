package com.astro.vajra.util

import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Resilient timezone parser used to handle legacy and user-provided timezone values.
 */
object TimezoneSanitizer {

    private val zoneTokenRegex = Regex("[A-Za-z_]+(?:/[A-Za-z0-9_+\\-]+)+")
    private val offsetTokenRegex = Regex("(?i)(?:UTC|GMT)?\\s*[+-]\\d{1,2}(?::?\\d{2})?")
    private val legacyZoneAliases = mapOf(
        "ASIA/KATMANDU" to "Asia/Kathmandu",
        "ASIA/CALCUTTA" to "Asia/Kolkata",
        "ASIA/SAIGON" to "Asia/Ho_Chi_Minh",
        "EUROPE/KIEV" to "Europe/Kyiv",
        "US/EASTERN" to "America/New_York",
        "US/CENTRAL" to "America/Chicago",
        "US/MOUNTAIN" to "America/Denver",
        "US/PACIFIC" to "America/Los_Angeles"
    )

    fun resolveZoneIdOrNull(rawTimezone: String?): ZoneId? {
        val trimmed = rawTimezone?.trim().orEmpty()
        if (trimmed.isEmpty()) return null

        parseZoneId(trimmed)?.let { return it }
        parseOffset(trimmed)?.let { return it }

        val candidates = linkedSetOf<String>()
        candidates.addAll(extractCandidates(trimmed))
        candidates.forEach { candidate ->
            parseZoneId(candidate)?.let { return it }
            parseOffset(candidate)?.let { return it }
        }

        return null
    }

    fun resolveZoneId(rawTimezone: String?): ZoneId {
        return resolveZoneIdOrNull(rawTimezone)
            ?: throw IllegalArgumentException("Invalid timezone identifier: ${rawTimezone?.trim().orEmpty()}")
    }

    fun normalizeTimezoneId(rawTimezone: String?): String {
        return resolveZoneId(rawTimezone).id
    }

    private fun parseZoneId(candidate: String): ZoneId? {
        val canonical = canonicalizeZoneCandidate(candidate)
        return runCatching { ZoneId.of(canonical) }.getOrNull()
    }

    private fun parseOffset(candidate: String): ZoneOffset? {
        val normalized = candidate
            .trim()
            .uppercase(Locale.ROOT)
            .replace("UTC", "")
            .replace("GMT", "")
            .replace(" ", "")

        if (normalized.isEmpty()) return null

        normalized.toDoubleOrNull()?.let { numericHours ->
            val totalSeconds = (numericHours * 3600.0).roundToInt().coerceIn(-18 * 3600, 18 * 3600)
            return ZoneOffset.ofTotalSeconds(totalSeconds)
        }

        if (!normalized.startsWith("+") && !normalized.startsWith("-")) return null

        val canonical = when {
            Regex("^[+-]\\d{1,2}$").matches(normalized) -> {
                val sign = normalized.first()
                val hours = normalized.drop(1).toIntOrNull() ?: return null
                String.format(Locale.ROOT, "%c%02d:00", sign, hours)
            }
            Regex("^[+-]\\d{3,4}$").matches(normalized) -> {
                val sign = normalized.first()
                val digits = normalized.drop(1)
                val hoursPart = if (digits.length == 3) digits.substring(0, 1) else digits.substring(0, 2)
                val minutesPart = digits.takeLast(2)
                val hours = hoursPart.toIntOrNull() ?: return null
                val minutes = minutesPart.toIntOrNull() ?: return null
                if (minutes !in 0..59) return null
                String.format(Locale.ROOT, "%c%02d:%02d", sign, hours, minutes)
            }
            Regex("^[+-]\\d{1,2}:\\d{2}$").matches(normalized) -> {
                val sign = normalized.first()
                val pieces = normalized.drop(1).split(":")
                if (pieces.size != 2) return null
                val hours = pieces[0].toIntOrNull() ?: return null
                val minutes = pieces[1].toIntOrNull() ?: return null
                if (minutes !in 0..59) return null
                String.format(Locale.ROOT, "%c%02d:%02d", sign, hours, minutes)
            }
            else -> null
        } ?: return null

        return runCatching { ZoneOffset.of(canonical) }.getOrNull()
    }

    private fun extractCandidates(raw: String): List<String> {
        val candidates = linkedSetOf<String>()

        zoneTokenRegex.findAll(raw).forEach { candidates += it.value }
        offsetTokenRegex.findAll(raw).forEach { candidates += it.value }

        raw.split(" ", ",", ";", "|")
            .map { it.trim().trim('(', ')', '[', ']', '{', '}', '"', '\'') }
            .filter { it.isNotBlank() }
            .forEach { candidates += it }

        return candidates.toList()
    }

    private fun canonicalizeZoneCandidate(candidate: String): String {
        val trimmed = candidate.trim()
        if (trimmed.isEmpty()) return trimmed
        return legacyZoneAliases[trimmed.uppercase(Locale.ROOT)] ?: trimmed
    }
}
