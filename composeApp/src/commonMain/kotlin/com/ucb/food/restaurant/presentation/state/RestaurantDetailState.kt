package com.ucb.food.restaurant.presentation.state

import com.ucb.food.restaurant.domain.model.DishModel
import com.ucb.food.restaurant.domain.model.RestaurantModel
import com.ucb.food.restaurant.domain.model.ReviewModel

data class RestaurantDetailState(
    val isLoading: Boolean = false,
    val restaurant: RestaurantModel? = null,
    val menu: List<DishModel> = emptyList(),
    val reviews: List<ReviewModel> = emptyList(),
    val error: String? = null
)
