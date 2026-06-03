package com.ucb.food.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.home.presentation.state.HomeEffect
import com.ucb.food.home.presentation.state.HomeEvent
import com.ucb.food.home.presentation.state.HomeState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnMenuClick -> {
                viewModelScope.launch { _effect.send(HomeEffect.OpenMenu) }
            }
            HomeEvent.OnProfileClick -> {
                viewModelScope.launch { _effect.send(HomeEffect.NavigateToProfile) }
            }
            HomeEvent.OnCartClick -> {
                viewModelScope.launch { _effect.send(HomeEffect.NavigateToCart) }
            }
            HomeEvent.OnLogoutClick -> {
                logout()
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            try {
                Firebase.auth.signOut()
                _effect.send(HomeEffect.NavigateToLogin)
            } catch (e: Exception) {
                // Silently fail or log
            }
        }
    }
}
