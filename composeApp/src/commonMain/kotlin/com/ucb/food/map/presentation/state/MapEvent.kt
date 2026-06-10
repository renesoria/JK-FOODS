package com.ucb.food.map.presentation.state

sealed interface MapEvent {
    data object OnLoadRequested : MapEvent
    data object OnBackClicked : MapEvent
}
