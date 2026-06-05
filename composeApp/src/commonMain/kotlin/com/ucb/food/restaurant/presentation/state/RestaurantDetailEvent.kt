package com.ucb.food.restaurant.presentation.state

sealed interface RestaurantDetailEvent {
    data class LoadDetails(val id: String) : RestaurantDetailEvent
    data object OnBackClick : RestaurantDetailEvent
    data object OnAddReviewClick : RestaurantDetailEvent
}
