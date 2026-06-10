package com.ucb.food.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.onboarding.domain.usecase.IsOnboardingCompletedUseCase
import com.ucb.food.profile.domain.usecase.SyncProfileUseCase
import com.ucb.food.login.domain.repository.AuthenticationRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase,
    private val authRepository: AuthenticationRepository,
    private val syncProfileUseCase: SyncProfileUseCase
) : ViewModel() {

    private val _startDestination = MutableStateFlow<NavRoute?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkAppStatus()
    }

    private fun checkAppStatus() {
        viewModelScope.launch {
            val isOnboardingCompleted = isOnboardingCompletedUseCase()
            val isUserLoggedIn = Firebase.auth.currentUser != null

            if (isUserLoggedIn) {
                // Ensure local database has the user info
                val localProfile = authRepository.getUserProfile().firstOrNull()
                if (localProfile == null) {
                    syncProfileUseCase()
                }
            }

            _startDestination.value = when {
                !isOnboardingCompleted -> NavRoute.Onboarding
                !isUserLoggedIn -> NavRoute.Login
                else -> NavRoute.Home
            }
        }
    }
}
