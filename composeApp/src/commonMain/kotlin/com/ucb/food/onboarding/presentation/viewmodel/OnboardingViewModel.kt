package com.ucb.food.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.ucb.food.onboarding.domain.usecase.GetOnboardingUseCase
import com.ucb.food.onboarding.presentation.state.OnboardingEvent
import com.ucb.food.onboarding.presentation.state.OnboardingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val getOnboardingUseCase: GetOnboardingUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingUiState())
    val state = _state.asStateFlow()

    init {
        loadOnboardingConfig()
    }

    private fun loadOnboardingConfig() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // In a real app, get the language from the system settings
                // For this exercise, we can use "es" as default or try to detect it
                val language = "es" 
                val items = getOnboardingUseCase(language)
                _state.update { it.copy(items = items, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.OnFinish -> {
                viewModelScope.launch {
                    completeOnboardingUseCase()
                }
            }
        }
    }
}
