package com.ucb.food.onboarding.domain.repository

import com.ucb.food.onboarding.domain.model.OnboardingItem

interface OnboardingRepository {
    suspend fun getOnboardingConfig(languageCode: String): List<OnboardingItem>
    suspend fun completeOnboarding()
    suspend fun isOnboardingCompleted(): Boolean
}
