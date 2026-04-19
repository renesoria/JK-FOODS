package com.ucb.food.country.presentation.state

sealed interface CountryEvent {
    data object OnLoad : CountryEvent
    data object OnRefresh : CountryEvent
}