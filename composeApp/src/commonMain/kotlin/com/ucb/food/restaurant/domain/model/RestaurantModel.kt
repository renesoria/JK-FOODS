package com.ucb.food.restaurant.domain.model

data class RestaurantModel(
    val id: String = "",
    val name: String = "",
    val logoUrl: String = "",
    val overallRating: Double = 0.0,
    val description: String = "",
    val branches: List<BranchModel> = emptyList()
)

data class BranchModel(
    val id: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val phone: String = ""
)
