package com.ucb.food.restaurant.presentation.state

sealed interface RestaurantDetailEffect {
    data object NavigateBack : RestaurantDetailEffect
    data class NavigateToAddReview(val restaurantId: String) : RestaurantDetailEffect
}
