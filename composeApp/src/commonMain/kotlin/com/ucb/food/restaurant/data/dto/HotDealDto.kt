package com.ucb.food.restaurant.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class HotDealDto(
    val id: String = "",
    val imageUrl: String = "",
    val restaurantId: String = "",
    val title: String = ""
)
