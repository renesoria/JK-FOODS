package com.ucb.food.restaurant.domain.model

data class DishModel(
    val id: String = "",
    val restaurantId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val imageUrl: String? = null
)
