package com.ucb.food.onboarding.domain.usecase

import com.ucb.food.onboarding.domain.model.OnboardingItem
import com.ucb.food.onboarding.domain.repository.OnboardingRepository

class GetOnboardingUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(languageCode: String): List<OnboardingItem> {
        return repository.getOnboardingConfig(languageCode)
    }
}
