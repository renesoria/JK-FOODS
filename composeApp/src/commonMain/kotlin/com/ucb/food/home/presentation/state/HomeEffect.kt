package com.ucb.food.home.presentation.state

sealed interface HomeEffect {
    data object OpenMenu : HomeEffect
    data object NavigateToProfile : HomeEffect
    data object NavigateToCart : HomeEffect
}