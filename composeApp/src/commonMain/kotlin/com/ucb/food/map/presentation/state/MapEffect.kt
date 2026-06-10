package com.ucb.food.map.presentation.state

sealed interface MapEffect {
    data object NavigateBack : MapEffect
}
