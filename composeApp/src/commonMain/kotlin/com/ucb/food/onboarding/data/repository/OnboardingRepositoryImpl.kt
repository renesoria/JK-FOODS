package com.ucb.food.onboarding.data.repository

import com.ucb.food.firebase.getRemoteConfigString
import com.ucb.food.onboarding.data.dto.OnboardingConfigDto
import com.ucb.food.onboarding.data.mapper.toDomain
import com.ucb.food.onboarding.domain.model.OnboardingItem
import com.ucb.food.onboarding.domain.repository.OnboardingRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.json.Json

class OnboardingRepositoryImpl(
    private val json: Json,
    private val settings: Settings
) : OnboardingRepository {

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }

    override suspend fun getOnboardingConfig(languageCode: String): List<OnboardingItem> {
        return try {
            val jsonString = getRemoteConfigString("onboarding_config")
            if (jsonString.isEmpty()) return emptyList()
            
            val dto = json.decodeFromString<OnboardingConfigDto>(jsonString)
            dto.config.map { it.toDomain(languageCode) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun completeOnboarding() {
        settings[KEY_ONBOARDING_COMPLETED] = true
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return settings.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }
}
