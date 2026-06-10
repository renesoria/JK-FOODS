package com.ucb.food.onboarding.domain.usecase

import com.ucb.food.onboarding.domain.repository.OnboardingRepository

class IsOnboardingCompletedUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(): Boolean = repository.isOnboardingCompleted()
}
