package com.ucb.food.restaurant.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DishDto(
    val id: String = "",
    val restaurantId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val imageUrl: String? = null
)
