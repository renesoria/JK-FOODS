package com.ucb.food.onboarding.presentation.state

import com.ucb.food.onboarding.domain.model.OnboardingItem

data class OnboardingUiState(
    val items: List<OnboardingItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface OnboardingEvent {
    data object OnFinish : OnboardingEvent
}
