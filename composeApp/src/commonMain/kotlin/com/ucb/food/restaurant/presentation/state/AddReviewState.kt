package com.ucb.food.restaurant.presentation.state

import com.ucb.food.restaurant.domain.model.DishModel
import com.ucb.food.restaurant.domain.model.RestaurantModel

data class AddReviewState(
    val isLoading: Boolean = false,
    val restaurant: RestaurantModel? = null,
    val menu: List<DishModel> = emptyList(),
    val rating: Int = 0,
    val comment: String = "",
    val photoBase64: String? = null,
    val selectedDishes: Set<String> = emptySet(),
    
    // Checklist attributes
    val hasParking: Boolean = false,
    val hasWifi: Boolean = false,
    val acceptsDigitalPayment: Boolean = false,
    val isPetFriendly: Boolean = false,
    val hasKidsArea: Boolean = false,
    
    // Quick tags
    val isFastService: Boolean = false,
    val isClean: Boolean = false,
    val isCozy: Boolean = false,
    
    // Price
    val isFairPrice: Boolean = false,
    
    val error: String? = null,
    val success: Boolean = false
)
