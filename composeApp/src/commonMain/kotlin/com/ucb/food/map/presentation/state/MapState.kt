package com.ucb.food.map.presentation.state

import com.ucb.food.restaurant.domain.model.RestaurantModel

data class MapState(
    val isLoading: Boolean = false,
    val restaurants: List<RestaurantModel> = emptyList(),
    val error: String? = null
)
