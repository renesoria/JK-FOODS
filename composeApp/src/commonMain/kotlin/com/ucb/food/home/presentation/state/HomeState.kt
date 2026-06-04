package com.ucb.food.home.presentation.state

import com.ucb.food.restaurant.domain.model.RestaurantModel

data class HomeState(
    val isLoading: Boolean = false,
    val restaurants: List<RestaurantModel> = emptyList(),
    val filteredRestaurants: List<RestaurantModel> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)
