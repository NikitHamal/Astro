package com.astro.storm.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Onboarding Manager for AstroStorm
 *
 * Manages the onboarding state to show the onboarding flow only once
 * on first app launch. The state is persisted to SharedPreferences.
 */
class OnboardingManager private constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _onboardingCompleted = MutableStateFlow(isOnboardingCompleted())
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    /**
     * Mark onboarding as completed
     */
    fun completeOnboarding() {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
        _onboardingCompleted.value = true
    }

    /**
     * Check if onboarding has been completed
     */
    private fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    /**
     * Reset onboarding state (for testing purposes)
     */
    fun resetOnboarding() {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, false).apply()
        _onboardingCompleted.value = false
    }

    companion object {
        private const val PREFS_NAME = "astro_storm_onboarding_prefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

        @Volatile
        private var INSTANCE: OnboardingManager? = null

        fun getInstance(context: Context): OnboardingManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: OnboardingManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
}
