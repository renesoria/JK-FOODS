package com.ucb.food.home.presentation.state

sealed interface HomeEvent {
    data object OnMenuClick : HomeEvent
    data object OnProfileClick : HomeEvent
    data object OnCartClick : HomeEvent
    data object OnLogoutClick : HomeEvent
    data class OnSearchQueryChanged(val query: String) : HomeEvent
    data class OnRestaurantClick(val id: String) : HomeEvent
    data object OnLogoClick : HomeEvent
    data object OnMapClick : HomeEvent
}
