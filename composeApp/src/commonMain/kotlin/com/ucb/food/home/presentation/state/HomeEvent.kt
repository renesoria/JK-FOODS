package com.ucb.food.home.presentation.state

sealed interface HomeEvent {
    data object OnMenuClick : HomeEvent
    data object OnProfileClick : HomeEvent
    data object OnCartClick : HomeEvent
    data object OnLogoutClick : HomeEvent
}