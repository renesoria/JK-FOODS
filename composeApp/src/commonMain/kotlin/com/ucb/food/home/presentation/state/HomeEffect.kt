package com.ucb.food.home.presentation.state

sealed interface HomeEffect {
    data object OpenMenu : HomeEffect
    data object NavigateToProfile : HomeEffect
    data object NavigateToCart : HomeEffect
    data object NavigateToLogin : HomeEffect
    data class NavigateToRestaurantDetail(val id: String) : HomeEffect
}
