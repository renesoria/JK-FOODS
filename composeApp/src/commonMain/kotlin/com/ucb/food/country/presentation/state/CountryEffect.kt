package com.ucb.food.country.presentation.state

sealed interface CountryEffect {
    data class ShowError(val message: String) : CountryEffect
}