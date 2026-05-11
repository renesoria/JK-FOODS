package com.ucb.food.home.presentation.state

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null
)