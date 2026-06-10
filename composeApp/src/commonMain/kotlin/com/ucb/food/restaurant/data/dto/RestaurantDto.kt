package com.ucb.food.restaurant.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantDto(
    val id: String = "",
    val name: String = "",
    val logoUrl: String = "",
    val overallRating: Double = 0.0,
    val description: String = "",
    val branches: List<BranchDto> = emptyList()
)

@Serializable
data class BranchDto(
    val id: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val phone: String = ""
)
