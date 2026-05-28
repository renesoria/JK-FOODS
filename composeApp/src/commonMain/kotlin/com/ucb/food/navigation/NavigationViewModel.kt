package com.ucb.food.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.onboarding.domain.usecase.IsOnboardingCompletedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase
) : ViewModel() {

    private val _startDestination = MutableStateFlow<NavRoute?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            val isCompleted = isOnboardingCompletedUseCase()
            _startDestination.value = if (isCompleted) NavRoute.Home else NavRoute.Onboarding
        }
    }
}
