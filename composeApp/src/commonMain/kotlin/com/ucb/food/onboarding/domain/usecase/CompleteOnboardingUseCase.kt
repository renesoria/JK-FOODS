package com.ucb.food.onboarding.domain.usecase

import com.ucb.food.onboarding.domain.repository.OnboardingRepository

class CompleteOnboardingUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke() {
        repository.completeOnboarding()
    }
}
